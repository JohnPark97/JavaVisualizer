const propertiesToViz = {
  name: 'Class',
  line_count: 'Line count',
  // IsEnumeration: 'Is Enum?',
  // IsInterface: 'Is Interface?',
  imports: 'Imports',
  methods: 'Methods',
  variables: 'Variables'
};
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

    vis.detail.hint = vis.detail.append('text')
      .attr('class', 'hint')
      .attr('y', 75)
      .attr('x', 10)
      .attr('display', '100%')
      .style('font-size', 25)
      .text('Hover a house to see its details!')
  }

  update() {
    let vis = this;

    vis.render();
  }

  render() {
    let vis = this;

    for (let klass of vis.data.classes) {
      if ((!vis.hoverClass) || // && !vis.selectedClass) ||
        (klass.name != vis.hoverClass)) continue; // && klass.name != vis.selectedClass)) continue;

      // Hide hint and previous class' details
      vis.detail.selectAll('.hint').attr('display', 'none');
      vis.detail.selectAll('.detail').remove();

      vis.detail
        .append('foreignObject')
        .attr('class', 'detail')
        .attr('width', vis.config.containerWidth)
        .attr('height', vis.config.containerHeight - 35)
        .attr('y', 35)
        .attr('x', 10)
        .attr('overflow', 'auto')
        .append('xhtml:div')
        .html(vis.getClassPropertyText(klass));
    };
  }

  getClassPropertyText(klass) {
    let vis = this;
    let label = '';

    Object.entries(propertiesToViz).forEach((prop, idx) => {
      label += '<p><b>';
      // Append 'Enum' or 'Interface' to 'Class' as appropriate
      if (prop[0] == 'name' && klass.IsEnumeration) label += 'Enum ';
      if (prop[0] == 'name' && klass.IsInterface) label += 'Interface ';

      label += `${prop[1]}:</b>`

      // Return N/A for empty fields
      if (!klass[prop[0]] || klass[prop[0]].length < 1) label += ' N/A';

      // Deconstruct imports
      if (prop[1] == 'Imports') label += `${vis.getImportText(klass)}`;

      // Deconstruct methods
      else if (prop[1] == 'Methods') label += `${vis.getMethodText(klass)}`;

      // Deconstruct variables
      else if (prop[1] == 'Variables') label += `${vis.getVariablesText(klass)}`;

      // Just print other properties
      else label += ` ${klass[prop[0]]}`;

      label += '</p>';
    });

    return label;
  };

  getMethodText(klass) {
    let text = '';

    Object.entries(klass.methods).forEach((m) => {
      const method = m[1];
      text += '</p><p>';
      text += `${method.modifiers.join(' ')} ${method.name}(`;
      Object.entries(method.parameters).forEach((p) => {
        const param = p[1];
        text += `${param.type} ${param.name} `;
      });
      text = `${text.trim()})`;
    });

    return text;
  }

  getVariablesText(klass) {
    let text = '';

    Object.entries(klass.variables).forEach((v) => {
      const variable = v[1];
      text += '</p><p>';
      text += `${variable.modifiers.join(' ')} ${variable.type} ${variable.name}`;
    });

    return text;
  }

  getImportText(klass) {
    let text = '';
    klass.imports.forEach((i) => {
      text += `</p><p>${i}`;
    });

    return text;
  }
}