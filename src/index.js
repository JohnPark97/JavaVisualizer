const testDataFilePath = '../assets/project.json';
// const testDataFilePath = '../assets/test_data.json';

const getRepoURL = () => {
  return new Promise((resolve) => {
    let repoURL;
    $('#submit').on('click', () => {
      repoURL = $('#repo-link').val();
      if (!repoURL) return window.alert('Please enter a Github repo');

      // validate URL?
      return resolve(repoURL);
    });
  });
};

const handleFileSubmission = () => {
  return new Promise((resolve) => {
    $('#file-selector').on('change', () => {
      const file = $('#file-selector').prop('files')[0];
      return resolve(file);
    });
  });
}

const main = async () => {
  let repoURL, res, testData, file, zip;
  try {
    /* To get data from backend */

    // repoURL = await getRepoURL();
    // res = await getData(repoURL);
    
    // TODO figure out a way to combine zipping and URL
    // file = await handleFileSubmission();
    // zip = await readFile(file);
    // res = await postZip(zip);

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