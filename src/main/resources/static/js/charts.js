$(document).ready(function () {
    var colors = ["#6F257F", "#3366cc", "#dc3912", "#ff9900", "#109618", "#990099", "#0099c6", "#dd4477", "#66aa00",
        "#b82e2e", "#316395", "#994499", "#22aa99", "#aaaa11", "#6633cc", "#e67300", "#8b0707",
        "#651067", "#329262", "#5574a6", "#3b3eac"];
    var current = 0;

    window.LineChart = (function () {
        function LineChart(title, container) {
            this.title = title;
            this.$container = $(container);
            this.color = colors[current];
            current = (current + 1) % colors.length;

            this._create();
        }

        LineChart.prototype = {
            update: function (data) {
                var parseTime = d3.time.format("%d-%b-%y").parse;
                var self = this;

                // process
                data.forEach(function (d) {
                    d.date = parseTime(d.date);
                    d.value = +d.value;
                });

                self.x.domain(d3.extent(data, function (d) { return d.date; }));
                self.y.domain([
                    Math.max(d3.min(data, function (d) { return d.value; }) - 10, 0),
                    d3.max(data, function (d) { return d.value; }) + 10
                ]);

                self.svg.select('.axis--x').transition().duration(300).call(self.xAxis);

                self.svg.select(".axis--y").transition().duration(300).call(self.yAxis);

                if (!self.path) {
                    self.path = self.g.append("path")
                        .datum(data)
                        .attr("class", "line")
                        .attr("d", self.line)
                        .style("stroke", self.color);

                    self.focus = self.g.append("g")
                        .attr("class", "focus")
                        .style("display", "none");

                    self.focus.append("line")
                        .attr("class", "x-hover-line hover-line")
                        .style("stroke", self.color)
                        .attr("y1", 0)
                        .attr("y2", self.height);

                    self.focus.append("line")
                        .attr("class", "y-hover-line hover-line")
                        .style("stroke", self.color)
                        .attr("x1", self.width)
                        .attr("x2", self.width);

                    self.focus.append("circle")
                        .style("stroke", self.color)
                        .attr("r", 7.5);

                    self.focus.append("text")
                        .attr("x", -10)
                        .attr("dy", "-1em");
                } else {
                    self.g.select(".line")
                        .transition()
                        .duration(300)
                        .attr("d", self.line(data));
                }

                self.svg.select("rect")
                    .on("mouseover", function () {
                        self.focus.style("display", null);
                    })
                    .on("mouseout", function () {
                        self.focus.style("display", "none");
                    })
                    .on("mousemove", function () {
                        var x0 = self.x.invert(d3.mouse(this)[0]),
                            i = self.bisectDate(data, x0, 1),
                            d0 = data[i - 1],
                            d1 = data[i],
                            d = x0 - d0.date > d1.date - x0 ? d1 : d0;
                        self.focus.attr("transform", "translate(" + self.x(d.date) + "," + self.y(d.value) + ")");
                        self.focus.select("text").text(function () {
                            return d.value + " (" + d.event + ")";
                        });
                        self.focus.select(".x-hover-line").attr("y2", self.height - self.y(d.value));
                        self.focus.select(".y-hover-line").attr("x2", self.width + self.width);
                    });
            },

            _create: function () {
                var chartWidth = this.$container.width() * 0.9;
                var chartHeight = this.$container.parent().parent().height() * 0.55;
                var margin = {top: 20, right: 20, bottom: 30, left: 40};
                var self = this;

                self.svg = d3.select(this.$container[0]).append("svg")
                    .attr("width", chartWidth)
                    .attr("height", chartHeight);

                self.width = chartWidth - margin.left - margin.right;
                self.height = chartHeight - margin.top - margin.bottom;

                self.x = d3.time.scale().range([0, self.width]);
                self.y = d3.scale.linear().range([self.height, 0]);

                self.line = d3.svg.line()
                    .x(function (d) {
                        return self.x(d.date);
                    })
                    .y(function (d) {
                        return self.y(d.value);
                    });

                self.g = self.svg.append("g")
                    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

                self.xAxis = d3.svg.axis().orient("bottom").scale(self.x);

                self.yAxis = d3.svg.axis().orient("left").scale(self.y).ticks(6).tickFormat(function (d) {
                    return d;
                });

                self.g.append("g")
                    .attr("class", "axis axis--x")
                    .attr("transform", "translate(0," + self.height + ")")
                    .call(self.xAxis);

                self.g.append("g")
                    .attr("class", "axis axis--y")
                    .call(self.yAxis)
                    .append("text")
                    .attr("class", "axis-title")
                    .attr("transform", "rotate(-90)")
                    .attr("y", 6)
                    .attr("dy", ".71em")
                    .style("text-anchor", "end")
                    .attr("fill", "#5D6971")
                    .text(self.title);

                self.bisectDate = d3.bisector(function (d) {
                    return d.date;
                }).left;

                self.svg.append("rect")
                    .attr("transform", "translate(" + margin.left + "," + margin.top + ")")
                    .attr("class", "overlay")
                    .attr("width", self.width)
                    .attr("height", self.height);
            }
        };

        return LineChart;
    })();
});
