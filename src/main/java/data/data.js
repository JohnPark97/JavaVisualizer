
const host = 'https://api.github.com';
// const repoOwnerEndpoint = '/repos/{owner}/{repo}';
const repoOwnerEndpoint = '/repos/dwmkerr/spaceinvaders'; // as an example

const backendHost = 'http://localhost:8000'
const testEndpoint = '/data';

var hash = new Object();


const getData = async (repoURL) => {
  const url = backendHost + testEndpoint;
  // TODO use the repoURL somehow
  console.log(url);

  const res = await httpRequest(url, {
    method: 'GET',
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

      inputHashMap(nextListOfFiles);
    } else {
      hash[element['name']] = element;
    }
  }
}

const getHashMap = async () => {

  hash = new Object();

  const url = host + repoOwnerEndpoint + '/contents/';
  console.log(url);

  const listOfFiles = await httpRequest(url, {
    method: 'GET',
  });


  await inputHashMap(listOfFiles);

  console.log("hash");
  console.log(hash);

  return hash;
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