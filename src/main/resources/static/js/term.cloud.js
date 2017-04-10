$(document).ready(function () {
    var fill = d3.scale.category20();
    var svg = d3.select("#term-cloud-canvas").append("svg")
        .attr("width", 1000)
        .attr("height", 500)
        .append("g")
        .attr("transform", "translate(500,250)");

    loadTermFrequencies(function (results) {
        var layout = d3.layout.cloud()
            .size([1000, 500])
            .words(results.map(function (r) {
                return {text: r.term, size: r.size};
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
    });

    function loadTermFrequencies(onSuccess) {
        $.ajax({
            url: "/term-frequencies",
            type: "GET",
            cache: false,
            contentType: "application/json; charset=utf-8",
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