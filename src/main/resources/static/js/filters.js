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
