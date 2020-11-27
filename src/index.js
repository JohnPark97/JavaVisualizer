const getRepoURL = () => {
  return new Promise((resolve) => {
    let repoURL;
    $('#submit').on('click', () => {
      repoURL = $('#repo-link').val();
      if (!repoURL) return window.alert('Please enter a Github repo');

      repoURL = repoURL.trim();
      if (!validateURL(repoURL)) return window.alert('URL must have the format github.com/USER/REPO');
      return resolve(repoURL);
    });
  });
};

const validateURL = (repoURL) => {
  try {
    const parts = repoURL.split('/');
    const index = parts.findIndex(p => p == 'github.com');
    return parts.length - 2 == (index + 1);
  } catch (err) {
    return false;
  }
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
  let res, data, zip;
  try {
    res = await Promise.race([getRepoURL(), handleFileSubmission()]);

    if (typeof res == "object") {
      // is a zip file
      zip = await readFile(res);
      data = await postZip(zip);
    } else {
      // is a repoURL
      data = await getData(res);
    }

    if (!data.classes || data.classes.length == 0) {
      window.alert('This project has no Java classes!');
      return main();
    }
  } catch (err) {
    console.log(err);
    window.alert('An error has occurred, please try again.');
    return main();
  }

  data = formatData(data);
  console.log(data);
  visualize(data);

  // Allow user to visualize another repo
  main();
};

main();