const propertiesToViz = {
  name: 'Class',
  smells: 'Code smells',
  line_count: 'Line count',
  // These fields are appended to the 'Class' text
  // IsEnumeration: 'Is Enum?', 
  // IsInterface: 'Is Interface?',
  imports: 'Imports',
  methods: 'Methods',
  variables: 'Variables',
};
class Detail {
  constructor(_config) {
    this.config = {
      parentElement: _config.parentElement,
      containerWidth: _config.containerWidth || 1000,
      containerHeight: _config.containerHeight || 600,
    }
    this.data = _config.data;
    this.init();
  }

  init() {
    let vis = this;

    d3.selectAll(`${vis.config.parentElement} *`).remove();

    vis.svg = d3.select(vis.config.parentElement)
      .attr('width', vis.config.containerWidth)
      .attr('height', vis.config.containerHeight)
      .attr('x', vis.config.containerWidth);

    vis.border = vis.svg.append('rect')
      .attr('x', 0)
      .attr('y', 0)
      .attr('width', vis.config.containerWidth)
      .attr('height', vis.config.containerHeight)
      .style('stroke', 'black')
      .style('fill', 'white')
      .style('stroke-width', 1);

    vis.config.titleYOffset = 35;
    vis.detail = vis.svg.append('g');
    vis.detail.append('text')
      .attr('y', vis.config.titleYOffset)
      .attr('x', 10)
      .style('font-size', 25)
      .style('font-weight', 'bold')
      .text('Details');

    vis.detail.hint = vis.detail.append('text')
      .attr('class', 'hint')
      .attr('y', 75)
      .attr('x', 10)
      .attr('display', '100%')
      .text('Hover a house to see its details!');
  }

  update() {
    let vis = this;

    vis.render();
  }

  renderBarGraph(klass) {
    let vis = this;

    vis.bargraph = new BarGraph({
      selection: vis.config.parentElement,
      class: klass,
      containerWidth: vis.config.containerWidth,
      containerHeight: vis.config.containerHeight / 2,
    });

    vis.bargraph.update();
  }

  render() {
    let vis = this;

    for (let klass of vis.data.classes) {
      if ((!vis.hoverClass) || (klass.name != vis.hoverClass)) continue;
      vis.renderClassDetail(klass);
      vis.renderBarGraph(klass);
    }
  }

  renderClassDetail(klass) {
    let vis = this;

    // Hide hint and previous class' details
    vis.detail.selectAll('.hint').attr('display', 'none');
    vis.detail.selectAll('.detailText').remove();

    vis.detail
      .append('foreignObject')
      .attr('class', 'detailText')
      .attr('width', vis.config.containerWidth)
      .attr('height', vis.config.containerHeight / 2 - vis.config.titleYOffset)
      .attr('y', 45)
      .attr('x', 10)
      .attr('overflow', 'auto')
      .append('xhtml:div')
      .html(vis.getClassPropertyText(klass))
      .call(vis.wrap, vis.config.containerWidth);
  };

  // based on https://bl.ocks.org/mbostock/7555321
  wrap(text, width) {
    let vis = this;
    text.each(() => {
      var text = d3.select(this);
      if (text.length > 0) {
        var words = text.text().split(/\s+/).reverse(),
          word,
          line = [],
          lineNumber = 1,
          lineHeight = 1.2, // ems
          y = text.attr("y"),
          dy = parseFloat(text.attr("dy")) || 0,
          tspan = text.text(null).append("tspan").attr("x", 0).attr("y", y).attr("dy", dy + "em");
        while (word = words.pop()) {
          line.push(word);
          tspan.text(line.join(" "));
          if (vis.getTextWidth(tspan.node()) > width) {
            line.pop();
            tspan.text(line.join(" "));
            line = [word];
            tspan = text.append("tspan").attr("x", 0).attr("y", y).attr("dy", (++lineNumber * lineHeight + dy) + "em").text(word);
          }
        }
      }
    });
  }

  getTextWidth(text, font) {
    // if given, use cached canvas for better performance
    // else, create new canvas
    var canvas = getTextWidth.canvas || (getTextWidth.canvas = document.createElement("canvas"));
    var context = canvas.getContext("2d");
    context.font = font;
    var metrics = context.measureText(text);
    return metrics.width;
  };

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

      // Deconstruct code smells
      if (prop[1] == 'Code smells') label += `${vis.getSmellsText(klass)}`;

      // Deconstruct imports
      else if (prop[1] == 'Imports') label += `${vis.getImportText(klass)}`;

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

  getSmellsText(klass) {
    let text = '';

    Object.entries(klass.smells).forEach((s) => {
      const smell = s[1];
      text += '</p><p>';
      text += `<u>${smell.name}</u>: ${smell.description}`;
    });

    return text;
  }

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