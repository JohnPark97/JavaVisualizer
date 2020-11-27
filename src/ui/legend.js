class Legend {
  constructor(_config) {
    this.config = {
      selection: _config.selection,
      containerWidth: _config.containerWidth || 1000,
      containerHeight: _config.containerHeight || 600,
    }
    this.data = _config.data;
  }

  update() {
    let vis = this;

    vis.render();
  }

  render() {
    let vis = this;

    console.log(vis.config);

    d3.select('.legend').remove();

    vis.svg = d3.select(vis.config.selection)
      .append('g')
      .attr('class', 'legend')
      .attr('width', vis.config.containerWidth)
      .attr('height', vis.config.containerHeight)
      .attr('transform', `translate(0, ${vis.config.containerHeight * 3 - vis.config.containerHeight})`);

    vis.border = vis.svg.append('rect')
      .attr('width', vis.config.containerWidth)
      .attr('height', vis.config.containerHeight)
      .style('stroke', 'black')
      .style('fill', 'white')
      .style('stroke-width', 1);

    vis.textG = vis.svg.append('g')
      .attr('transform', `translate(10, 25)`);
    vis.collectionG = vis.svg.append('g');

    // Title
    vis.title = vis.textG.append('text')
      .text('Legend')
      .attr('text-decoration', 'underline')
      .style('font-weight', 'bold');

    const dependencyTextYOffset = 33;
    const dependencyArrowYOffset = 35;
    const lineEnd = vis.config.containerWidth - 50;
    vis.textG.append('text')
      .text('has a dependency on')
      .attr('transform', `translate(20, ${dependencyTextYOffset - 10})`);
    vis.textG.append('text')
      .text('B')
      .attr('transform', `translate(${lineEnd + 15}, ${dependencyTextYOffset})`);
    vis.textG.append('text')
      .text('A')
      .attr('transform', `translate(0, ${dependencyTextYOffset})`);

    // Arrow line
    vis.textG.append('path')
      .attr('d', d3.line()([[20, dependencyArrowYOffset], [lineEnd, dependencyArrowYOffset]]))
      .attr('stroke', 'black')
      .attr('fill', 'none');
    // Arrow tip
    vis.textG.append('path')
      .attr('d', d3.line()([[lineEnd, 25], [lineEnd, 45], [vis.config.containerWidth - 40, dependencyArrowYOffset]]))
      .attr('stroke', 'black');

    vis.collectionG
      .attr('transform', `translate(10, 95)`);
    vis.collectionG.append('text')
      .text('Collection type')
      .attr('text-decoration', 'underline')
      .attr('font-weight', 'bold')

    vis.data.collections.forEach((c, idx) => {
      const yOffset = dependencyTextYOffset + (30 * idx);
      const lineYOffset = yOffset + 5;
      vis.collectionG.append('text')
        .text(c)
        .attr('transform', `translate(0, ${yOffset})`);
      vis.collectionG.append('path')
        .attr('d', d3.line()([[0, lineYOffset], [lineEnd, lineYOffset]]))
        .attr('stroke', vis.colourScale(c))
      .attr('stroke-width', 3);
    });
  }
}