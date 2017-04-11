$(document).ready(function () {
    $("#slider").slider({
        orientation: "vertical",
        range: true,
        values: [2016, 2016],
        min: 2006,
        max: 2016,
        step: 1,
        slide: function (event, ui) {
            console.log(ui.values[0]);
            console.log(ui.values[1]);
        }
    });
});
