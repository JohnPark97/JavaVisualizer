class Legend {
  constructor(_config) {
    this.config = {
      parentElement: _config.parentElement,
      containerWidth: _config.containerWidth || 1000,
      containerHeight: _config.containerHeight || 600,
    }
    this.initVis();
  }

  initVis() {
    let vis = this;
    
    d3.selectAll(`${vis.config.parentElement} *`).remove();


    vis.config.containerHeight = vis.config.containerHeight / 3;

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
  }

  update() {
    let vis = this;

    vis.render();
  }

  render() {
  }
}