$(document).ready(function () {
    var $slider = $("#slider");
    var loadingTrendSelection = false;
    var charts = [];

    window.setupFilters = function (dateRange) {
        $slider.labeledslider({
            orientation: "vertical",
            range: true,
            values: [dateRange.from, dateRange.to],
            min: dateRange.from,
            max: dateRange.to,
            step: 1,
            slide: function (event, ui) {
                if (ui.values[0] === ui.values[1])
                    return false;
                console.log(ui.values[0]);
                console.log(ui.values[1]);
                return true;
            }
        });

        charts.push(new LineChart("Assailant Age", "#chart-age-canvas"));
        charts.push(new LineChart("Victims Count", "#chart-victims-canvas"));

        $("#search-terms").on("click", loadTrendSelection);

        loadTrendSelection();
    };

    function loadTrendSelection() {
        if (loadingTrendSelection)
            return;
        loadingTrendSelection = true;
        var dates = $slider.labeledslider("values");
        var data = { from: dates[0], to: dates[1] };

        $.ajax({ url: "/trend-selection", data: data }).done(function (results) {
            console.log(results);
            charts[0].update(map(results, "assailantAge"));
            charts[1].update(map(results, "victimCount"));
            loadingTrendSelection = false;
        });
    }

    function map(results, fieldName) {
        return results.trends.map(function(t) { return { date: t.date, event: t.event, value: t[fieldName] }; })
    }
});
