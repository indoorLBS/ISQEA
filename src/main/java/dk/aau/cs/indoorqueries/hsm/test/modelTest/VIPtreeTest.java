package dk.aau.cs.indoorqueries.hsm.test.modelTest;

import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.iPTree.VIPTree;
import dk.aau.cs.indoorqueries.hsm.datagenerate.ClassifyPartition;
import dk.aau.cs.indoorqueries.hsm.datagenerate.HSMDataGenRead;

import java.io.IOException;

/**
 * test VIP-Tree
 * @author Tiantian Liu
 */
public class VIPtreeTest {
    public static void main(String[] arg) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMem = runtime.totalMemory() - runtime.freeMemory();

        long start = System.nanoTime();

        HSMDataGenRead dateGenReadMen = new HSMDataGenRead();
        dateGenReadMen.dataGen();

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        ClassifyPartition classifyPartition = new ClassifyPartition();
        classifyPartition.classifyPar();

//        ObjectGen objectGen = new ObjectGen();
//        objectGen.readObjects(1500);

        VIPTree vipTree = VIPTree.createTree();
        vipTree.initVIPtree();

        long end = System.nanoTime();
        long endMem = runtime.totalMemory() - runtime.freeMemory();

        double time = (double) (end - start) / 1000 / 1000;
        double memory = (double) (endMem - startMem) / 1024 / 1024;
        System.out.println("time: " + time + " ms");
        System.out.println("memory: " + memory + " mb");
//


//        for (int i = 1; i <= vipTree.levels.size(); i++) {
//            System.out.println("level: " + i);
//            ArrayList<Node> nodes = vipTree.getNodes(i);
//            System.out.println("node size: " + nodes.size());
//            int levelPartitionSize = 0;
//            for (int j = 0; j < nodes.size(); j++) {
//                Node tempNode = nodes.get(j);
////                System.out.println("height: " + tempNode.getHeight());
//                System.out.println("node: " + tempNode.toString());
////                System.out.println("partition: " + tempNode.getmPartitions());
////                System.out.println("children: " + tempNode.getmChildren());
////                System.out.println("accessDoor: " + tempNode.getAccessDoors());
////                System.out.println("objects: " + tempNode.getAllObjects());
////                System.out.println("partition size: " + tempNode.getmPartitions().size());
//                levelPartitionSize += tempNode.getmPartitions().size();
//            }
//
//            if (i == 1) {
//                for (int j = 0; j < nodes.size(); j++) {
//                    Node tempNode = nodes.get(j);
//                    System.out.println("nodeId: " + tempNode.getNodeID());
////                    ArrayList<Integer> doors = tempNode.getAccessDoors();
////                    doors.addAll(tempNode.getInternalDoors());
////
////                    for (int k = 0; k < doors.size(); k++) {
////                        for (int h = k + 1; h < doors.size(); h++) {
////                            int d1 = doors.get(k);
////                            int d2 = doors.get(h);
////                            if (doors.get(k) == doors.get(h)) continue;
////                            System.out.println(d1 + "-" + d2 + ": " + tempNode.getDist(d1, d2));
////                            System.out.println(d2 + "-" + d1 + ": " + tempNode.getDist(d2, d1));
////
////                        }
////                    }
//
////                    System.out.println("nodeId: " + tempNode.getNodeID());
//                    HashMap<String, String> distMatrix = ((LeafNode)tempNode).getDistMatrixVIP();
//                    Iterator iterator = distMatrix.keySet().iterator();
//                    while (iterator.hasNext()) {
//                        Object key = iterator.next();
//                        System.out.println(key + " " + distMatrix.get(key));
////                        String [] keyArr = ((String)key).split("-");
////                        String keyConvert = keyArr[1] + "-" + keyArr[0];
////                        System.out.println(keyConvert + " " + ((LeafNode)tempNode).getDistVIP(Integer.parseInt(keyArr[1]), Integer.parseInt(keyArr[0])));
////                        System.out.println(key + " " + tempNode.getDist(Integer.parseInt(keyArr[0]), Integer.parseInt(keyArr[1])));
//                    }
//
////                    Iterator iterator = ((LeafNode)tempNode).getObjectDist().keySet().iterator();
////                    while (iterator.hasNext()) {
////                        Object key = iterator.next();
////                        System.out.println(key + " " + ((LeafNode)tempNode).getObjectDist().get(key));
////                    }
//                }
//            }
//            System.out.println("level partition size: " + levelPartitionSize);
//            System.out.println();
//        }

    }
}
