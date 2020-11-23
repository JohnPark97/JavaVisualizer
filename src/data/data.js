const host = 'https://api.github.com';
const backendHost = 'http://localhost:8000';
const testEndpoint = '/data';
const dataFromZipEndpoint = '/dataFromZip';

const extensions = ["js", "css", "java"];

let hash = new Object();

const repoFilesEndpoint = (owner, repo) => {
  return `/repos/${owner}/${repo}/contents/`;
};

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
  console.log(await map);

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

  console.log("hash");
  console.log(hash);

  return hash;
}

const isCodeFile = (name) => {
  let extension = name.split(".");
  extension = extension[extension.length - 1];

  return extensions.includes(extension);
}

const readFile = async (zip) => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.addEventListener('load', (event) => {
      const res = event.target.result;
      resolve(res);
    });
    reader.readAsArrayBuffer(zip);
  });
}

const postZip = async (zip) => {
  const url = backendHost + dataFromZipEndpoint;

  return await httpRequest(url, {
    method: 'POST',
    body: zip,
  });
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