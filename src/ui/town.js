class Town {
  constructor(_config) {
    this.config = {
      parentElement: _config.parentElement,
      containerWidth: _config.containerWidth || 1000,
      containerHeight: _config.containerHeight || 600,
    }
    this.detail = _config.detail;
    this.data = _config.data;
    this.initVis();
  }

  initVis() {
    let vis = this;

    d3.selectAll(`${vis.config.parentElement} *`).remove();

    vis.svg = d3.select(vis.config.parentElement)
      .attr('width', vis.config.containerWidth - 100)
      .attr('height', vis.config.containerHeight);

    vis.border = vis.svg.append('rect')
      .attr('x', 0)
      .attr('y', 0)
      .attr('width', vis.config.containerWidth)
      .attr('height', vis.config.containerHeight)
      .style('stroke', 'black')
      .style('fill', 'white')
      .style('stroke-width', 1);

    vis.town = vis.svg.append('g');

    vis.svg.call(d3.zoom().on('zoom', () => {
      vis.town.attr('transform', d3.event.transform);
    }));;

    vis.legend = new Legend({
      selection: vis.config.parentElement,
      data: vis.data,
      containerWidth: vis.config.containerWidth / 5,
      containerHeight: vis.config.containerHeight / 2,
    });
  }

  update() {
    let vis = this;

    vis.houseScale = d3.scaleLinear()
      .domain(d3.extent(vis.data.classes, d => d.line_count))
      .range([10, 100]);

    vis.garbageScale = d3.scaleLinear()
      .domain(d3.extent(vis.data.classes, d => d.smells.length))
      .range([0, d3.max(vis.houseScale.range())]);

    vis.colourScale = d3.scaleOrdinal(d3.schemeCategory10)
      .domain(vis.data.collections);

    const customColours = vis.colourScale.range();
    const index = vis.data.collections.findIndex((c) => c === 'Regular dependency');
    customColours[index] = 'black';
    vis.colourScale.range(customColours);

    vis.legend.data = vis.data;
    vis.legend.colourScale = vis.colourScale;
    vis.legend.update();

    vis.render();
  }

  render() {
    let vis = this;

    vis.runForceSimulation();
    vis.renderHouse();
  }

  runForceSimulation() {
    let vis = this;

    vis.svg.append('defs').append('marker')
      .attr('id', 'arrowhead')
      .attr('viewBox', '-0 -5 10 10')
      .attr('refX', 9)
      .attr('refY', 0)
      .attr('orient', 'auto')
      .attr('markerWidth', 13)
      .attr('markerHeight', 13)
      .attr('xoverflow', 'visible');

    vis.svg.selectAll('marker')
      .append('svg:path')
      .attr('d', 'M 0,-2 L 4 ,0 L 0,2')
      .attr('fill', 'black')
      .style('stroke', 'none');

    vis.simulation = d3.forceSimulation()
      .force('link', d3.forceLink().id((d) => d.name).distance(150).strength(1)) // TODO make a scale for distance?
      .force('x', d3.forceX(vis.config.containerWidth / 2).strength(0.4))
      .force('y', d3.forceY(vis.config.containerHeight / 2).strength(0.6))
      .force('charge', d3.forceManyBody().strength(-7000))
      .force('collision', d3.forceCollide().radius(d => vis.houseScale(d.line_count)))
      .nodes(vis.data.classes)
      .on('tick', () => {
        vis.links
          .attr('x1', (d) => d.source.x)
          .attr('y1', (d) => d.source.y)
          .attr('x2', (d) => d.target.x)
          .attr('y2', (d) => d.target.y);

        vis.nodes
          .attr('transform', (d) => `translate(${d.x}, ${d.y})`);
      });

    vis.linksG = vis.town.append('g')
      .attr('class', 'links');
    vis.links = vis.linksG.selectAll('.link')
      .data(vis.data.links)
      .enter().append('line')
      .attr('class', 'link')
      .attr('marker-end', `url(#arrowhead)`)
      .style('stroke', (d) => vis.colourScale(d.type[0]));

    vis.nodesG = vis.town.append('g')
      .attr('class', 'nodes')
    vis.nodes = vis.nodesG.selectAll('.node')
      .data(vis.data.classes)
      .enter().append('g')
      .attr('class', 'node')
      .call(d3.drag()
        .on('start', (d) => {
          if (!d3.event.active) vis.simulation.alpha(0.3).restart();
          d.fx = d.x;
          d.fy = d.y;
        })
        .on('drag', (d) => {
          d.fx = d3.event.x;
          d.fy = d3.event.y;
        })
        .on('end', (d) => {
          if (!d3.event.active) vis.simulation.alphaTarget(0);
          d.fx = null;
          d.fy = null;
        })
      )
      .on('mouseover', (d) => {
        vis.detail.hoverClass = d.name;
        vis.detail.update();
      })
      .on('mouseout', () => {
        vis.detail.hoverClass = null;
        vis.detail.update();
      });

    vis.simulation.force('link')
      .links(vis.data.links)
  }

  renderHouse() {
    let vis = this;

    // House
    vis.house = vis.nodes.append('g');
    vis.house.append('rect')
      .attr('class', 'house')
      .attr('width', d => vis.houseScale(d.line_count))
      .attr('height', d => vis.houseScale(d.line_count))
      .attr('y', d => `${vis.houseScale(d.line_count) * -1}`)
      .style('stroke', 'black')
      .style('fill', '#ffe88a');

    // Roof
    const trianglePoints = (d) =>
      `0 0, ${vis.houseScale(d.line_count)} 0, \
      ${vis.houseScale(d.line_count) / 2} -${vis.houseScale(d.line_count) / 2} ${vis.houseScale(d.line_count) / 2}, \
      -${vis.houseScale(d.line_count) / 2} 0 0`;

    vis.house.append('polyline')
      .attr('points', trianglePoints)
      .attr('transform', d => `translate(0, ${vis.houseScale(d.line_count) * -1})`)
      .style('fill', (d) => d.IsEnumeration ? 'green' : d.IsInterface ? 'blue' : 'red')
      .style('stroke', 'black');

    // Door
    vis.house.append('rect')
      .attr('width', d => vis.houseScale(d.line_count) / 4)
      .attr('height', d => vis.houseScale(d.line_count) / 2)
      .attr('x', d => vis.houseScale(d.line_count) / 5 * 2)
      .attr('y', d => vis.houseScale(d.line_count) / 2 - vis.houseScale(d.line_count))
      .style('fill', 'brown');

    // Windows
    vis.house.append('rect')
      .attr('width', d => vis.houseScale(d.line_count) / 4)
      .attr('height', d => vis.houseScale(d.line_count) / 4)
      .attr('x', d => vis.houseScale(d.line_count) / 7)
      .attr('y', d => vis.houseScale(d.line_count) / 7 - vis.houseScale(d.line_count))
      .style('fill', 'white');
    vis.house.append('rect')
      .attr('width', d => vis.houseScale(d.line_count) / 4)
      .attr('height', d => vis.houseScale(d.line_count) / 4)
      .attr('x', d => vis.houseScale(d.line_count) / 7 * 4)
      .attr('y', d => vis.houseScale(d.line_count) / 7 - vis.houseScale(d.line_count))
      .style('fill', 'white');

    // House label
    vis.house.append('rect')
      .attr('width', 12)
      .attr('height', d => d.name.length * 9)
      .attr('transform', d => `translate(0, 13) rotate(-90)`)
      .style('fill', 'white')
      .style('opacity', 0.8)
    vis.house.append('text')
      .attr('class', 'house-label')
      .attr('dy', 13) // use font-size instead of 10 for more dynamic?
      .attr('font-weight', 'bold')
      .text(d => d.name);

    const magicNumber = d => vis.garbageScale(d.smells.length) / vis.houseScale(d.line_count) * vis.houseScale(d.line_count);
    vis.house.append('svg:image')
      .attr('x', d => -magicNumber(d))
      .attr('y', d => -magicNumber(d))
      .attr('width', d => magicNumber(d))
      .attr('height', d => magicNumber(d))
      .attr('xlink:href', 'assets/garbage.png');
  }
}