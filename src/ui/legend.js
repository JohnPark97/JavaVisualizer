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

    d3.select('.legend').remove();

    vis.svg = d3.select(vis.config.selection)
      .append('g')
      .attr('class', 'legend')
      .attr('width', vis.config.containerWidth)
      .attr('height', vis.config.containerHeight)
      .attr('transform', `translate(0, ${vis.config.containerHeight * 2 - vis.config.containerHeight})`);

    vis.border = vis.svg.append('rect')
      .attr('width', vis.config.containerWidth)
      .attr('height', vis.config.containerHeight)
      .style('stroke', 'black')
      .style('fill', '#e6e6e6')
      .style('stroke-width', 1)
      .attr('rx', 10)
      .attr('ry', 10);

    const textGYOffset = 25;
    vis.textG = vis.svg.append('g')
      .attr('transform', `translate(10, ${textGYOffset})`);

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

    const collectionGYOffset = textGYOffset + dependencyArrowYOffset + dependencyTextYOffset
    vis.collectionG = vis.svg.append('g')
      .attr('transform', `translate(10, ${collectionGYOffset})`);
    vis.collectionG.append('text')
      .text('Dependency type')
      .attr('text-decoration', 'underline')
      .attr('font-weight', 'bold');

    let lineYOffset = 0;
    const arrowHeight = 15;
    vis.data.collections.forEach((c, idx) => {
      const text = c;
      const yOffset = dependencyTextYOffset + (30 * idx);
      lineYOffset = yOffset + 5;
      const svg = vis.collectionG;
      svg.append('text')
        .text(text)
        .attr('transform', `translate(0, ${yOffset})`);
      svg.append('path')
        .attr('d', d3.line()([[0, lineYOffset], [lineEnd, lineYOffset]]))
        .attr('stroke', vis.colourScale(text))
        .attr('stroke-width', 3);
      svg.append('path')
        .attr('d', d3.line()([[lineEnd, lineYOffset - arrowHeight], [lineEnd, lineYOffset + arrowHeight], [vis.config.containerWidth - 25, lineYOffset]]))
        .attr('fill', 'black')
        .attr('stroke', 'black');
    });

    const classGYOffset = collectionGYOffset + lineYOffset + arrowHeight * 3;
    vis.classG = vis.svg.append('g')
      .attr('transform', `translate(10, ${classGYOffset})`);

    vis.classG.append('text')
      .text('Roofs denote Class type')
      .attr('text-decoration', 'underline')
      .style('font-weight', 'bold');
    const classTypes = ['Class', 'Enum', 'Interface'];
    classTypes.forEach((t, idx) => {
      const xOffset = vis.config.containerWidth / 3 * idx;
      const trianglePoints = `0 0, 30 0, 15 -15 15, -15 0 0`;
      vis.classG.append('polyline')
        .attr('points', trianglePoints)
        .attr('transform', d => `translate(${xOffset}, ${dependencyArrowYOffset})`)
        .style('fill', t == 'Enum' ? 'green' : t == 'Interface' ? 'blue' : 'red')
        .style('stroke', 'black');
      vis.classG.append('text')
        .text(t)
        .attr('transform', `translate(${xOffset}, ${dependencyTextYOffset + 15})`);
    });
  }
}