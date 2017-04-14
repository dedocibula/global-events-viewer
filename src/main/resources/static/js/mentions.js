$(document).ready(function () {
    $("#events-container").find("input").checkboxradio({
        icon: false,
        direction: "vertical"
    });

    var mappings = {};
    $("#mentions-container").find("ul").each(function () { mappings[this.id] = $(this); });

    $("input[name=event]").on("change", function (e) {
        mappings["urls-" + e.target.id].show().siblings().hide();
    });
});
