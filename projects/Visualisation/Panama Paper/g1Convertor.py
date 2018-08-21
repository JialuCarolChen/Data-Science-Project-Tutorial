def get_edgelist(filePath):
    # a Hashmap to store and Edges
    edges = list()
    # read file and put information into hashmap
    with open(filePath, "r") as myfile:
        parent_node = ''
        for line in myfile:
            line = str(line).split()
            # print(line)
            if (len(line) > 2):
                node_name = ""
                for i in range(0, len(line) - 1):
                    if (i == 0):
                        node_name = line[i]
                    else:
                        node_name = node_name + " " + line[i]
                line = [node_name, line[-1]]
            # print(line)
            # get the most current parent_node
            if ('{' in line):
                # print(line[0].replace(':', ''))
                parent_node = line[0].replace(':', '')
                # print(parent_node)
            # get the target node and weight
            elif (len(line) == 2 and ('{' not in line)):
                child = line[0].replace(':', '')
                weight = line[1]
                edges.append([parent_node, child, weight])
    return edges


# target file path
filePath = "panama.txt"
edges = get_edgelist(filePath)

#converting the data to a csv file:
#Node table: Id, Label
#Edge table: Source, Target, Label, Weight












