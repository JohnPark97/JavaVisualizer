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
      await inputHashMap(nextListOfFiles);
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

const formatType = (dependency) => {
  const collections = dependency.Collection;
  const name = dependency.DependencyName;
  if (collections.length < 1) return [];
  return collections[0][name].Collection;

  // If this needs to be dynamic
  collections.forEach((c) => {
    console.log(c[name].Collection);
  });
  return ret;
}

const formatData = (data) => {
  let links = [];

  data.links.forEach((link) => {
      link.Dependencies.forEach((dependency) => {
        links.push({
          source: link.Class,
          target: dependency.ClassNames[0],
          type: formatType(dependency),
        });
      });
  });

  return { classes: data.classes, links };
}