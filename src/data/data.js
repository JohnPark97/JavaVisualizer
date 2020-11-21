const host = 'https://api.github.com';
// const repoOwnerEndpoint = '/repos/{owner}/{repo}';
const repoOwnerEndpoint = '/repos/dwmkerr/spaceinvaders'; // as an example

const backendHost = 'http://localhost:8000'
const testEndpoint = '/data';

const extensions = ["js", "css", "java"];

let hash = new Object();

const getData = async (repoURL) => {
  const url = backendHost + testEndpoint;
  console.log(url);
  console.log(repoURL);

  let map = getHashMap(repoURL);

  // send github url to backend
  const res = await httpRequest(url, {
    method: 'POST', // *GET, POST, PUT, DELETE, etc.
    headers: {
      'Content-Type': 'application/json'
    },
    body: repoURL // body data type must match "Content-Type" header
  });


  console.log("map");
  console.log(map);

  return res;

};

async function inputHashMap(listOfFiles) {
  for (const element of listOfFiles) {
    if (element['type'] === 'dir') {
      const url = element['url'];
      const nextListOfFiles = await httpRequest(url, {
        method: 'GET',
      });
      inputHashMap(nextListOfFiles);
    } else if (element['type'] === 'file' && isCodeFile(element['name'])) {
      hash[element['name']] = element;
    }
  }
}

const getHashMap = async (url) => {

  const repoUrl = url + '/contents/';

  const listOfFiles = await httpRequest(repoUrl, {
    method: 'GET',
  });

  await inputHashMap(listOfFiles);

  console.log("hash");
  console.log(hash);

  return hash;
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