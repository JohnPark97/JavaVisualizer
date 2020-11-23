class Town {
  constructor(_config) {
    this.config = {
      parentElement: _config.parentElement,
      containerWidth: _config.containerWidth || 1000,
      containerHeight: _config.containerHeight || 600,
    }
    this.detail = _config.detail;
    this.initVis();
  }

  initVis() {
    let vis = this;

    vis.svg = d3.select(vis.config.parentElement)
      .attr('width', vis.config.containerWidth)
      .attr('height', vis.config.containerHeight);

    vis.border = vis.svg.append('rect')
      .attr('x', 0)
      .attr('y', 0)
      .attr('width', vis.config.containerWidth)
      .attr('height', vis.config.containerHeight)
      .style('stroke', 'black')
      .style('fill', 'none')
      .style('stroke-width', 1);

    vis.town = vis.svg.append('g');

    vis.svg.call(d3.zoom().on('zoom', () => {
      vis.town.attr('transform', d3.event.transform);
    }));;
  }

  update() {
    let vis = this;

    vis.render();
  }

  render() {
    let vis = this;

    // TODO eventually use an actual scale
    const linecountScale = (d) => 15 + Math.sqrt(d.line_count);

    // vis.svg.append('defs').append('marker')
    //   .attr('id', 'arrowhead')
    //   .attr('viewBox', '-0 -5 10 10')
    //   .attr('refX', 13)
    //   .attr('refY', 0)
    //   .attr('orient', 'auto')
    //   .attr('markerWidth', 13)
    //   .attr('markerHeight', 13)
    //   .attr('xoverflow', 'visible');

    // vis.svg.selectAll('marker')
    //   .append('svg:path')
    //   .attr('d', 'M 0,-5 L 10 ,0 L 0,5')
    //   .attr('fill', '#999')
    //   .style('stroke', 'none');

    vis.simulation = d3.forceSimulation()
      .force('link', d3.forceLink().id((d) => d.name).distance(150).strength(1)) // TODO make a scale for distance
      .force('charge', d3.forceManyBody())
      .force('center', d3.forceCenter(vis.config.containerWidth / 2, vis.config.containerHeight / 2))
      .force('collision', d3.forceCollide().radius(linecountScale))
      .nodes(vis.data.classes)
      .on('tick', () => {
        vis.links
          .attr('x1', (d) => d.source.x)
          .attr('y1', (d) => d.source.y)
          .attr('x2', (d) => d.target.x)
          .attr('y2', (d) => d.target.y);

        vis.nodes
          .attr('transform', (d) => `translate(${d.x}, ${d.y})`);
      });;

    vis.links = vis.town.selectAll('.link')
      .data(vis.data.links)
      .enter().append('line')
      .attr('class', 'link')
      .attr('marker-end', 'url(#arrowhead)')

    // vis.links.append('title').text((d) => d.source);

    // vis.edgepaths = svg.selectAll('.edgepath')
    //   .data(vis.data.links)
    //   .enter().append('path')
    //   .attr('class', 'edgepath')
    //   .attr('fill-opacity', 0)
    //   .attr('stroke-opacity', 0)
    //   .attr('id', (d, i) => 'edgepath' + i)
    //   .style('pointer-events', 'none');

    vis.nodes = vis.town.selectAll('.node')
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
        }))
      .on('click', d => {
        vis.detail.selectedClass =
          vis.detail.selectedClass = d.name;
        vis.detail.update();
      });

    // House
    vis.nodes.append('rect')
      .attr('width', linecountScale)
      .attr('height', linecountScale)
      .style('stroke', 'black')
      .style('fill', '#ffe88a');

    // Roof
    const trianglePoints = (d) => `0 0, ${linecountScale(d)} 0, ${linecountScale(d) / 2} -${linecountScale(d) / 2} ${linecountScale(d) / 2}, -${linecountScale(d) / 2} 0 0`;
    vis.nodes.append('polyline')
      .attr('points', trianglePoints)
      .style('fill', 'red')
      .style('stroke', 'black');

    // Door
    vis.nodes.append('rect')
      .attr('width', d => linecountScale(d) / 4)
      .attr('height', d => linecountScale(d) / 2)
      .attr('x', d => linecountScale(d) / 5 * 2)
      .attr('y', d => linecountScale(d) / 2)
      .style('fill', 'brown');

    // Windows
    vis.nodes.append('rect')
      .attr('width', d => linecountScale(d) / 4)
      .attr('height', d => linecountScale(d) / 4)
      .attr('x', d => linecountScale(d) / 7)
      .attr('y', d => linecountScale(d) / 7)
      .style('fill', 'white');
    vis.nodes.append('rect')
      .attr('width', d => linecountScale(d) / 4)
      .attr('height', d => linecountScale(d) / 4)
      .attr('x', d => linecountScale(d) / 7 * 4)
      .attr('y', d => linecountScale(d) / 7)
      .style('fill', 'white');

    vis.nodes.append('rect')
      .attr('width', 12)
      .attr('height', d => d.name.length * 9)
      .attr('transform', d => `translate(0, ${linecountScale(d) + 12}) rotate(-90)`)
      .style('fill', 'white')
      .style('opacity', 0.8)
    vis.nodes.append('text')
      .attr('class', 'house-label')
      .attr('dy', d => linecountScale(d) + 12) // use font-size instead of 10 for more dynamic
      .attr('font-weight', 'bold')
      .text(d => d.name);

    vis.simulation
      .force('link')
      .links(vis.data.links);
  }
}