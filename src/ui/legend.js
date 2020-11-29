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

    vis.dependencyTextYOffset = 33;
    vis.dependencyArrowYOffset = 35;
    vis.lineEnd = vis.config.containerWidth - 50;
    vis.textG.append('text')
      .text('has a dependency on')
      .attr('transform', `translate(20, ${vis.dependencyTextYOffset - 10})`);
    vis.textG.append('text')
      .text('B')
      .attr('transform', `translate(${vis.lineEnd + 15}, ${vis.dependencyTextYOffset})`);
    vis.textG.append('text')
      .text('A')
      .attr('transform', `translate(0, ${vis.dependencyTextYOffset})`);
    // Arrow line
    vis.textG.append('path')
      .attr('d', d3.line()([[20, vis.dependencyArrowYOffset], [vis.lineEnd, vis.dependencyArrowYOffset]]))
      .attr('stroke', 'black')
      .attr('fill', 'none');
    // Arrow tip
    vis.textG.append('path')
      .attr('d', d3.line()([[vis.lineEnd, 25], [vis.lineEnd, 45], [vis.config.containerWidth - 40, vis.dependencyArrowYOffset]]))
      .attr('stroke', 'black');

    const collectionGYOffset = textGYOffset + vis.dependencyArrowYOffset + vis.dependencyTextYOffset
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
      const yOffset = vis.dependencyTextYOffset + (30 * idx);
      lineYOffset = yOffset + 5;
      const svg = vis.collectionG;
      svg.append('text')
        .text(text)
        .attr('transform', `translate(0, ${yOffset})`);
      svg.append('path')
        .attr('d', d3.line()([[0, lineYOffset], [vis.lineEnd, lineYOffset]]))
        .attr('stroke', vis.colourScale(text))
        .attr('stroke-width', 3);
      svg.append('path')
        .attr('d', d3.line()([[vis.lineEnd, lineYOffset - arrowHeight], [vis.lineEnd, lineYOffset + arrowHeight], [vis.config.containerWidth - 25, lineYOffset]]))
        .attr('fill', 'black')
        .attr('stroke', 'black');
    });

    const classGYOffset = collectionGYOffset + lineYOffset + arrowHeight * 3;
    vis.classG = vis.svg.append('g')
      .attr('transform', `translate(10, ${classGYOffset})`);

    vis.text = vis.theme === 'Spider' ? 'Amount of traffic' : 'Roofs denote Class type';
    vis.classG.append('text')
      .text(vis.text)
      .attr('text-decoration', 'underline')
      .style('font-weight', 'bold');

    if (vis.theme === 'Spider') {
      vis.renderSpiderLegend();
    } else {
      vis.renderHouseLegend();
    }
  }

  renderSpiderLegend() {
    let vis = this;
    const svg = vis.classG;

    const spiderImgs = [{
      path: 'spider.png',
      text: 'Heavy',
    },
    {
      path: 'spider-big.png',
      text: 'Medium',
    },
    {
      path: 'spider-reg.png',
      text: 'Low',
    }];
    const imgSize = 60;
    spiderImgs.forEach((t, idx) => {
      const xOffset = vis.config.containerWidth / 3 * idx;
      svg.append('svg:image')
        .attr('class', 'house')
        .attr('transform', `translate(${xOffset}, ${vis.dependencyArrowYOffset / 4})`)
        .attr('width', imgSize)
        .attr('height', imgSize)
        .attr('xlink:href', `assets/${t.path}`);
      svg.append('text')
        .text(t.text)
        .attr('transform', `translate(${xOffset}, ${vis.dependencyTextYOffset / 3 * 2 + imgSize})`);
    });
  }

  renderHouseLegend() {
    let vis = this;
    const classTypes = ['Class', 'Enum', 'Interface'];
    classTypes.forEach((t, idx) => {
      const xOffset = vis.config.containerWidth / 3 * idx;
      const trianglePoints = `0 0, 30 0, 15 -15 15, -15 0 0`;
      vis.classG.append('polyline')
        .attr('points', trianglePoints)
        .attr('transform', d => `translate(${xOffset}, ${vis.dependencyArrowYOffset})`)
        .style('fill', t == 'Enum' ? 'green' : t == 'Interface' ? 'blue' : 'red')
        .style('stroke', 'black');
      vis.classG.append('text')
        .text(t)
        .attr('transform', `translate(${xOffset}, ${vis.dependencyTextYOffset + 15})`);
    });
  }
}