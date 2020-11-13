let town;
let detail;

const visualize = (data) => {
  const windowWidth = getWindowWidth();

  detail = new Detail({
    parentElement: '#detail',
    containerWidth: windowWidth / 3,
  });
  town = new Town({
    parentElement: '#viz',
    containerWidth: windowWidth / 3 * 2, // use 2/3 of the window's width for main viz
    detail,
  });
  // load in data
  town.data = data;
  detail.data = data;

  // update and render
  town.update();
  detail.update();
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