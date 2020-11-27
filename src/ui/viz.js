let town;
let detail;

const visualize = (data) => {
  const windowWidth = getWindowWidth();
  const windowHeight = getWindowHeight() - 50 // - document.getElementById('viz-container').offsetTop - 10;

  detail = new Detail({
    parentElement: '#detail',
    containerWidth: windowWidth / 4,
    containerHeight: windowHeight,
    data: data,
  });
  town = new Town({
    parentElement: '#viz',
    containerWidth: windowWidth / 4 * 3, // use 3/4 of the window's width for main viz
    containerHeight: windowHeight,
    detail,
    data: data,
  });

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

const getWindowHeight = () => {
  return Math.max(
    document.body.scrollHeight,
    document.documentElement.scrollHeight,
    document.body.offsetHeight,
    document.documentElement.offsetHeight,
    document.documentElement.clientHeight
  );
};