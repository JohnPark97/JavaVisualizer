class BarGraph {
  constructor(_config) {
    this.config = {
      selection: _config.selection,
      containerWidth: _config.containerWidth || 1000,
      containerHeight: _config.containerHeight || 600,
    }
    this.class = _config.class;
    this.margin = {
      left: 25,
      right: 25,
      top: 25,
      bottom: 25,
    };
    this.init();
  }

  init() {
    let vis = this;

    d3.select('.bargraph').remove();

    vis.svg = d3.select(vis.config.selection)
      .append('g')
      .attr('class', 'bargraph')
      .attr('width', vis.config.containerWidth)
      .attr('height', vis.config.containerHeight)
      .attr('transform', `translate(0, ${vis.config.containerHeight})`);

    vis.width = vis.config.containerWidth - vis.margin.left - vis.margin.right;
    // vis.width = vis.config.containerWidth;
    vis.height = vis.config.containerHeight - vis.margin.top - vis.margin.bottom;
    // vis.height = vis.config.containerHeight;

    vis.border = vis.svg.append('rect')
      .attr('width', vis.config.containerWidth)
      .attr('height', vis.config.containerHeight)
      .attr('class', 'border')
      .style('stroke', 'black')
      .style('fill', 'white')
      .style('stroke-width', 1)

    vis.chart = vis.svg.append('g')
      .attr('transform', `scale(0.9, 0.9) translate(${vis.margin.left},${vis.margin.top + 15}) `)
      .attr('class', 'chart');

    vis.title = vis.chart.append('text')
      .attr('x', vis.config.containerWidth / 3)
      .attr('y', -vis.margin.top / 2)
      .text('Code Smell Occurences')

    vis.yAxisG = vis.chart.append('g')
      .attr('class', 'yAxisG')
      .attr('transform', `translate(${vis.margin.left}, 0)`);
    vis.xAxisG = vis.chart.append('g')
      .attr('class', 'xAxisG');

    vis.xAxisLabel = 'Code smell';
    vis.yAxisLabel = '# occurences';
    vis.yValue = d => d.count;
    vis.xValue = d => d.name;

    vis.yScale = d3.scaleLinear()
      .domain([0, d3.max(vis.class.smellCount, vis.yValue)])
      .range([vis.height, 0])
      .nice();
    const yAxis = d3.axisLeft(vis.yScale)

    // Draw chart elements
    vis.yAxisG.call(yAxis);
    vis.yAxisG.append('text')
      .attr('class', 'axis-label')
      .attr('y', -30)
      .attr('x', -vis.height / 2)
      .attr('transform', `rotate(-90)`)
      .attr('text-anchor', 'middle')
      .style('fill', 'black')
      .text(vis.yAxisLabel);

    vis.xAxisG.attr('transform', `translate(${vis.margin.left},${vis.height})`);
    vis.xAxisG.append('text')
      .attr('class', 'axis-label')
      .attr('y', 40)
      .attr('x', vis.width / 2)
      .style('fill', 'black')
      .text(vis.xAxisLabel);
  }

  update() {
    let vis = this;

    vis.render();
  }

  render() {
    let vis = this;

    const xScale = d3.scaleBand()
      .domain(vis.class.smellCount.map(d => d.name))
      .range([0, vis.width])
      .padding(0.2);

    const xAxis = d3.axisBottom(xScale);
    vis.xAxisG.call(xAxis);

    const rects = vis.chart.selectAll('rect')
      .data(vis.class.smellCount.sort((a, b) => b.count - a.count));
    rects.enter().append('rect')
      .attr('class', 'bar')
      .merge(rects)
      .on('mouseover', d => {
        vis.hoveredSmell = d.name;
        this.render();
      })
      .on('mouseout', () => {
        vis.hoveredSmell = null;
        this.render();
      })
      .attr('fill', d => vis.hoveredSmell === d.name ? '#71361c' : '#4682b4')
      .attr('y', d => vis.yScale(vis.yValue(d)))
      .attr('x', d => xScale(vis.xValue(d)) + vis.margin.left)
      .attr('width', xScale.bandwidth())
      .attr('height', d => (vis.yScale(0) - vis.yScale(vis.yValue(d))))
      .append('title')
      .text(d => `${vis.xValue(d)}: ${vis.yValue(d)}`);
  }
}