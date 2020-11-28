let town;
let detail;

const visualize = (data) => {
  const windowWidth = document.documentElement.clientWidth;
  const windowHeight = document.documentElement.clientHeight - 50;

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