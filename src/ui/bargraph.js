class BarGraph {
  constructor(_config) {
    this.config = {
      selection: _config.selection,
      containerWidth: _config.containerWidth || 1000,
      containerHeight: _config.containerHeight || 600,
      titleYOffset: _config.titleYOffset,
    }
    this.class = _config.class;
    this.margin = {
      left: 25,
      right: 25,
      top: 25,
      bottom: 25,
    };
    this.displayType = _config.displayType;
    this.init();
  }

  init() {
    let vis = this;

    vis.dataToDisplay = (vis.displayType === 'all') ? vis.class : vis.class.smellCount;
    vis.dataToDisplay.sort((a, b) => b.count - a.count);
    vis.dataToDisplay = (vis.displayType === 'all') ? vis.dataToDisplay.slice(0, 5) : vis.dataToDisplay;

    d3.select('.bargraph').remove();

    vis.svg = d3.select(vis.config.selection)
      .append('g')
      .attr('class', 'bargraph')
      .attr('width', vis.config.containerWidth)
      .attr('height', vis.config.containerHeight)
      .attr('transform', `translate(0, ${vis.displayType === 'all' ? vis.config.titleYOffset + 10 : vis.config.containerHeight})`);

    vis.width = vis.config.containerWidth - vis.margin.left - vis.margin.right;
    vis.height = vis.config.containerHeight - vis.margin.top - vis.margin.bottom;

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
      .attr('x', vis.config.containerWidth / 4)
      .attr('y', -vis.margin.top / 2)
      .text(`${(vis.displayType === 'all') ? 'Top 5 ' : ''}Code Smell Occurences`)

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
      .domain([0, d3.max(vis.dataToDisplay, vis.yValue)])
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

    if (vis.dataToDisplay.length < 1) {
      vis.chart.append('text')
        .text('No code smells in this class')
        .attr('x', vis.width / 4)
        .attr('y', vis.height / 2);
    }

    const xScale = d3.scaleBand()
      .domain(vis.dataToDisplay.map(d => d.name))
      .range([0, vis.width])
      .padding(0.2);

    const xAxis = d3.axisBottom(xScale);
    vis.xAxisG.call(xAxis);

    let data = vis.dataToDisplay.sort((a, b) => b.count - a.count);
    data = (vis.displayType === 'all') ? data.slice(0, 5) : data;
    const rects = vis.chart.selectAll('rect')
      .data(data);
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