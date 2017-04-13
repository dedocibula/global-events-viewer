$(document).ready(function () {
    var fill = d3.scale.category20();
    var $container = $("#term-cloud-canvas");
    var width = $container.width() * 0.9;
    var height = $container.parent().height() * 0.9;
    var svg = d3.select($container[0]).append("svg")
        .attr("width", width)
        .attr("height", height)
        .append("g")
        .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

    window.redrawTermCloud = function(results) {
        var layout = d3.layout.cloud()
            .size([width, height])
            .words(results.map(function (r) {
                return {text: r.term, size: (Math.log(r.frequency) / Math.log(10)) * Math.log(width) };
            }))
            .padding(5)
            .rotate(function () {
                return ~~(Math.random() * 2) * 90;
            })
            .font("Impact")
            .fontSize(function (d) {
                return d.size;
            })
            .on("end", draw);

        layout.start();
    };

    function draw(terms) {
        var cloud = svg.selectAll("g text")
            .data(terms, function (d) {
                return d.text;
            });

        //Entering terms
        cloud.enter()
            .append("text")
            .style("font-family", "Impact")
            .style("fill", function (d, i) {
                return fill(i);
            })
            .attr("text-anchor", "middle")
            .attr('font-size', 1)
            .text(function (d) {
                return d.text;
            });

        //Entering and existing terms
        cloud.transition()
            .duration(600)
            .style("font-size", function (d) {
                return d.size + "px";
            })
            .attr("transform", function (d) {
                return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
            })
            .style("fill-opacity", 1);

        //Exiting terms
        cloud.exit()
            .transition()
            .duration(200)
            .style('fill-opacity', 1e-6)
            .attr('font-size', 1)
            .remove();
    }
});