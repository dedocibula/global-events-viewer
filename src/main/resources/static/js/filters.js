$(document).ready(function () {
    var $slider = $("#slider");
    var $counter = $("#counter");
    var $events = $("#events");
    var $termCloudTitle = $("#term-cloud").find("h2:first");
    var lastDates = [-1, -1];
    var loadingTermSelection = false;

    window.setupFilters = function (dateRange) {
        $slider.labeledslider({
            orientation: "vertical",
            range: true,
            values: [dateRange.from, dateRange.to],
            min: dateRange.from,
            max: dateRange.to,
            step: 1,
            slide: function (event, ui) {
                console.log(ui.values[0]);
                console.log(ui.values[1]);
            }
        });

        $counter.spinner({
            min: 20,
            max: 100,
            step: 10
        });

        $("#search-terms").on("click", loadTermSelection);

        $(document.body).on("click", "#term-cloud-canvas text", displayTermMentions);

        loadTermSelection();
    };

    function loadTermSelection() {
        if (loadingTermSelection)
            return;
        loadingTermSelection = true;
        var dates = $slider.labeledslider("values");
        var count = $counter.spinner("value");
        var eventIds = [];
        if (dates[0] === lastDates[0] && dates[1] === lastDates[1])
            eventIds = $events.find(':checked').map(function() { return this.id }).toArray();
        var data = {
            from: dates[0],
            to: dates[1],
            count: count,
            eventIds: eventIds
        };

        post("/terms-selection", data, function (results) {
            var text = results.from === results.to ? results.from : results.from + " - " + results.to;
            $termCloudTitle.text("Most Popular Terms (" + text + ")");
            generateEventSection(results.events);
            redrawTermCloud(results.termFrequencies);
            lastDates = dates;
            loadingTermSelection = false;
        });
    }

    function displayTermMentions() {
        var dates = $slider.labeledslider("values");
        var eventIds = [];
        if (dates[0] === lastDates[0] && dates[1] === lastDates[1])
            eventIds = $events.find(':checked').map(function() { return this.id }).toArray();
        var data = {
            from: dates[0],
            to: dates[1],
            eventIds: eventIds
        };

        window.location = "/mentions/" + this.innerHTML + "?" + $.param(data);
    }

    function post(url, data, onSuccess) {
        $.ajax({
            url: url,
            type: "POST",
            cache: false,
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            data: data,
            dataType: "json",
            success: function (data) {
                /*<![CDATA[*/
                if (onSuccess && $.isFunction(onSuccess))
                    onSuccess(data);
                /*]]>*/
            },
            error: function (jqXHR, status, error) {
                console.log("Error In submitting request! : " + error);
            }
        });
    }

    function generateEventSection(events) {
        $events.empty();
        for (var i = events.length - 1; i >= 0; i--) {
            var event = events[i];
            $events.append('<label for="' + event.id + '">' + event.name + '</label>' +
                '<input class="toggle brand-toggle" type="checkbox" name="' + event.id + '" id="' + event.id + '" ' +
                (event.selected ? 'checked="checked"' : "") + '/>');
        }
        $events.controlgroup({ direction: "vertical" });
    }
});

(function ($, undefined) {


    $.widget("ui.labeledslider", $.ui.slider, {

        version: "@VERSION",

        options: {
            tickInterval: 0,
            tweenLabels: true,
            tickLabels: null,
            tickArray: []
        },

        uiSlider: null,
        tickInterval: 0,
        tweenLabels: true,

        _create: function () {

            this._detectOrientation();

            this.uiSlider =
                this.element
                    .wrap('<div class="ui-slider-wrapper ui-widget"></div>')
                    .before('<div class="ui-slider-labels"></div>')
                    .parent()
                    .addClass(this.orientation)
                    .css('font-size', this.element.css('font-size'));

            this._super();

            this.element.removeClass('ui-widget')

            this._alignWithStep();

            if (this.orientation == 'horizontal') {
                this.uiSlider
                    .width(this.element.css('width'));
            } else {
                this.uiSlider
                // .height( this.element.css('height') );
            }

            this._drawLabels();
        },

        _drawLabels: function () {

            var labels = this.options.tickLabels || {},
                $lbl = this.uiSlider.children('.ui-slider-labels'),
                dir = this.orientation == 'horizontal' ? 'left' : 'bottom',
                min = this.options.min,
                max = this.options.max,
                inr = this.tickInterval,
                cnt = ( max - min ),
                tickArray = this.options.tickArray,
                ta = tickArray.length > 0,
                label, pt,
                i = 0;

            $lbl.html('');

            for (; i <= cnt; i++) {

                if (( !ta && i % inr == 0 ) || ( ta && tickArray.indexOf(i + min) > -1 )) {

                    label = labels[i + min] ? labels[i + min] : (this.options.tweenLabels ? i + min : '');

                    $('<div>').addClass('ui-slider-label-ticks')
                        .css(dir, (Math.round(( i / cnt ) * 10000) / 100) + '%')
                        .html('<span>' + ( label ) + '</span>')
                        .appendTo($lbl);

                }
            }

        },

        _setOption: function (key, value) {

            this._super(key, value);

            switch (key) {

                case 'tickInterval':
                case 'tickLabels':
                case 'tickArray':
                case 'min':
                case 'max':
                case 'step':

                    this._alignWithStep();
                    this._drawLabels();
                    break;

                case 'orientation':

                    this.element
                        .removeClass('horizontal vertical')
                        .addClass(this.orientation);

                    this._drawLabels();
                    break;
            }
        },

        _alignWithStep: function () {
            if (this.options.tickInterval < this.options.step)
                this.tickInterval = this.options.step;
            else
                this.tickInterval = this.options.tickInterval;
        },

        _destroy: function () {
            this._super();
            this.uiSlider.replaceWith(this.element);
        },

        widget: function () {
            return this.uiSlider;
        }

    });

}(jQuery));
