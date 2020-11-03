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
  let repoURL;
  try {
    repoURL = await getRepoURL();
    await getData(repoURL);
  } catch {
    // do nothing for now
  }

  // mocked data
  data = {

  };
  visualize(data);
};

main();

