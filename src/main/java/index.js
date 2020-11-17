const testDataFilePath = '../assets/test_data.json';

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
  let repoURL, res, testData;
  try {
    /* To get data from backend */
    // repoURL = await getRepoURL();
    // res = await getData(repoURL);
  } catch (err) {
    console.log(err);
  }

  // mocked data
  res = await fetch(testDataFilePath);
  testData = await res.json();
  testData = formatData(testData);
  console.log(testData);
  visualize(testData);
};

main();

