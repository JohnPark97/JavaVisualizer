let town = new Town({parentElement: '#viz'});

const visualize = (data) => {
  // load in data
  town.data = data;

  // update and render
  town.update();
};

