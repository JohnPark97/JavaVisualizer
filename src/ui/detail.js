class Detail {
  constructor(_config) {
    this.config = {
      parentElement: _config.parentElement,
      containerWidth: _config.containerWidth || 1000,
      containerHeight: _config.containerHeight || 600,
      margin: {
        right: 75,
      },
    }
    this.init();
  }

  init() {
    let vis = this;

    vis.containerWidth = vis.config.containerWidth - vis.config.margin.right;

    vis.svg = d3.select(vis.config.parentElement)
      .attr('width', vis.containerWidth)
      .attr('height', vis.config.containerHeight)
      .attr('x', vis.config.containerWidth);

    vis.border = vis.svg.append('rect')
      .attr('x', 0)
      .attr('y', 0)
      .attr('width', vis.containerWidth)
      .attr('height', vis.config.containerHeight)
      .style('stroke', 'black')
      .style('fill', 'none')
      .style('stroke-width', 1);

    vis.detail = vis.svg.append('g');
    vis.detail.append('text')
      .attr('y', 35)
      .attr('x', 10)
      .style('font-size', 25)
      .style('font-weight', 'bold')
      .text('Details')
  }

  update() {
    let vis = this;

    // vis.selectedClass;
    console.log(vis.selectedClass);

    vis.render();
  }

  render() {
    let vis = this;

    if (!vis.selectedClass) {
      vis.detail.hint = vis.detail.append('text')
        .attr('class', 'hint')
        .attr('y', 75)
        .attr('x', 10)
        .style('font-size', 25)
        .text('Click a house to see its details!')
        .exit().remove();
      return;
    }

    vis.detail.hint = vis.detail.selectAll('.hint')
    vis.detail.hint.remove();

    const properties = {
      name: 'Class',
      line_count: 'Line count',
      IsEnumeration: 'Is Enum?',
      IsInterface: 'Is Interface?',
      imports: 'Imports', 
      methods: 'Methods',
      variables: 'Variables'
    };

    vis.detail.selectAll('.detail').remove();
    vis.detail.text = vis.detail.selectAll('.detail')
      .data(vis.data.classes);
    Object.entries(properties).forEach((prop, idx) => {
      vis.detail.text
        .enter().append('text')
        .attr('class', 'detail')
        .attr('y', 75)
        .attr('x', 10)
        .attr('dy', 30 * (idx))
        .style('font-size', 25)
        .merge(vis.detail.text)
        .text(klass => klass.name == vis.selectedClass ? vis.getClassPropertyText(klass, prop) : null)
        .exit().remove();
    });
  }

  getClassPropertyText(klass, prop) {
    let label = `${prop[1]}: ${klass[prop[0]]}`;
    // if (klass.name == 'SpaceInvaderVisitor') {
    //   console.log(prop[0]);
    // }
    // if (prop[0] == 'IsEnumeration' && klass[prop[0]]) label = 'Enum ' + label;
    // if (prop[0] == 'IsInterface' && klass[prop[0]]) label = 'Interface ' + label;

    // if (!klass[prop[0]]) label = 'N/A' TODO make fields that don't exist for classes show up as N/A
    return label;
  }
}