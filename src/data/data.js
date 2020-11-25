const host = 'https://api.github.com';
// const repoOwnerEndpoint = '/repos/{owner}/{repo}';
// '/repos/dwmkerr/spaceinvaders'; // as an example
const repoFilesEndpoint = (owner, repo) => {
  return `/repos/${owner}/${repo}/contents/`;
};

const backendHost = 'http://localhost:8000'
const testEndpoint = '/data';

const extensions = ["js", "css", "java"];

let returnedList = [];

const getData = async (repoURL) => {
  const url = backendHost + testEndpoint;
  console.log(url);
  console.log(repoURL);

  let resultList = await getHashMap(repoURL);
  const returnedString = resultList.toString();

  console.log("RESULT")
  console.log(resultList);

  console.log("String");
  console.log(returnedString);



  // send github url to backend
  const res = await httpRequest(url, {
    method: 'POST', // *GET, POST, PUT, DELETE, etc.
    headers: {
      'Content-Type': 'application/json'
    },
    body: returnedString // body data type must match "Content-Type" header
  });

  return res;

};

async function inputHashMap(listOfFiles) {
  for (const element of listOfFiles) {
    if (element['type'] === 'dir') {
      const url = element['url'];
      const nextListOfFiles = await httpRequest(url, {
        method: 'GET',
      });
      await inputHashMap(nextListOfFiles);
    } else if (element['type'] === 'file' && isCodeFile(element['name'])) {
      const stringElement = element['download_url'];
      returnedList.push(stringElement);
    }
  }
}

const getOwnerAndRepo = (url) => {
  const urlParts = url.split('/');
  const i = urlParts.findIndex(e => e == 'github.com')
  return {
    owner: urlParts[i + 1],
    repo: urlParts[i + 2],
  };
};

const getHashMap = async (url) => {

  const {
    owner,
    repo,
  } = getOwnerAndRepo(url);

  const repoUrl = host + repoFilesEndpoint(owner, repo);
  console.log(repoUrl);

  const listOfFiles = await httpRequest(repoUrl, {
    method: 'GET',
  });

  await inputHashMap(listOfFiles);

  console.log("returnedList");
  console.log(returnedList);

  return returnedList;
}

const isCodeFile = (name) => {
  let extension = name.split(".");
  extension = extension[extension.length - 1];

  if (extensions.includes(extension)) {
    return true;
  } else {
    return false;
  }

}

const formatData = (data) => {
  let classes = []
  let links = [];
  data.classes.forEach((klass) => {
    Object.entries(klass).forEach((prop) => {
      classes.push({
        className: prop[0],
        lineCount: prop[1],
      });
    });
  });

  data.links.forEach((link) => {
    Object.entries(link).forEach((relationship) => {
      relationship[1].forEach((target) => {
        links.push({
          source: relationship[0],
          target,
        });
      });
    });
  });
  console.log(classes)
  console.log(links)

  return { classes, links };
}