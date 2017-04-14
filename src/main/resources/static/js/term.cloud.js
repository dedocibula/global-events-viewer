$(document).ready(function () {
    var fill = d3.scale.category20();
    var scale = d3.scale.log().range([15, 100]);
    var $container = $("#term-cloud-canvas");
    var width = $container.width() * 0.9;
    var height = $container.parent().height() * 0.9;
    var svg = d3.select($container[0]).append("svg")
        .attr("width", width)
        .attr("height", height)
        .append("g")
        .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");
    var tooltip = d3.select($container[0])
        .append("div")
        .attr("class", "words-hoverover");

    window.redrawTermCloud = function (results) {
        var max = results[0].frequency;
        var min = results[results.length - 1].frequency;
        scale.domain([min, max]);
        var layout = d3.layout.cloud()
            .size([width, height])
            .words(results.map(function (r) {
                return {text: r.term, frequency: r.frequency };
            }))
            .padding(5)
            .rotate(function () {
                return ~~(Math.random() * 2) * 90;
            })
            .font("Impact")
            .fontSize(function (d) {
                return scale(d.frequency);
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
            })
            .on("mouseover", function (d) {
                tooltip.style('display', 'block');
            })
            .on("mouseout", function (d) {
                tooltip.style('display', 'none');
            })
            .on("mousemove", function (d) {
                var position = d3.mouse(svg[0][0].parentNode.parentNode);
                tooltip
                    .style('top', (position[1] + 20) + "px")
                    .html(d.frequency)
                    .style('left', (position[0]) + "px")
            });

        //Entering and existing terms
        cloud.transition()
            .duration(600)
            .style("font-size", function (d) {
                return scale(d.frequency) + "px";
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