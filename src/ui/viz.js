let town;
let detail;
let legend;

const visualize = (data) => {
  const windowWidth = getWindowWidth();

  detail = new Detail({
    parentElement: '#detail',
    containerWidth: windowWidth / 3,
  });
  legend = new Legend({
    parentElement: '#legend',
    containerWidth: windowWidth / 5,
  });
  town = new Town({
    parentElement: '#viz',
    containerWidth: windowWidth / 3 * 2, // use 2/3 of the window's width for main viz
    detail,
    legend,
  });

  // load in data
  town.data = data;
  detail.data = data;
  legend.data = data;

  // update and render
  town.update();
  detail.update();
  legend.update();
};

const getWindowWidth = () => {
  return Math.max(
    document.body.scrollWidth,
    document.documentElement.scrollWidth,
    document.body.offsetWidth,
    document.documentElement.offsetWidth,
    document.documentElement.clientWidth
  );
};