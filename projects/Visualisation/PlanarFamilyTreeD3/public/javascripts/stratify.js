d3.csv('data/greek-gods.csv', (data) => {
  var root = d3.stratify()
            .id((d) => { return d.name; })
            .parentId((d) => { return d.father; })
            (data);
  console.log(root);
});  
