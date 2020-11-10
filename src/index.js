const getRepoURL = () => {
  return new Promise((resolve, reject) => {
    let repoURL;
    $('#submit').on('click', () => {
      repoURL = $('#repo-link').val();
      if (!repoURL) return window.alert('Please enter a Github repo');
      
      // validate URL?
      return resolve(repoURL);
    });
  });
};

const main = async () => {
  let repoURL, res;
  try {
    repoURL = await getRepoURL();
    res = await getData(repoURL);
  } catch (err) {
    console.log(err);
    // do nothing for now
  }

  // mocked data
  data = {

  };
  data = res;
  visualize(data);
};

main();

