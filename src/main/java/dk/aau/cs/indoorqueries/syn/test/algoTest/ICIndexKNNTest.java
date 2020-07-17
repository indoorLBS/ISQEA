package dk.aau.cs.indoorqueries.syn.test.algoTest;

import com.github.davidmoten.rtree3d.Leaf;
import com.github.davidmoten.rtree3d.Node;
import com.github.davidmoten.rtree3d.NonLeaf;
import com.github.davidmoten.rtree3d.RTree;
import com.github.davidmoten.rtree3d.geometry.Box;
import com.github.davidmoten.rtree3d.geometry.Geometry;
import dk.aau.cs.indoorqueries.common.algorithm.ICIndex_KNN;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Partition;
import dk.aau.cs.indoorqueries.common.rStarTree3D.TreeNode;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.syn.datagenerate.DataGen;
import dk.aau.cs.indoorqueries.syn.datagenerate.ObjectGen;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

/**
 * test KNNQ using ICIndex
 * @author Tiantian Liu
 */
public class ICIndexKNNTest {
    private static <T extends Geometry> void print(Node<Integer, Box> node, int depth)
            throws FileNotFoundException {

        PrintStream out = new PrintStream("target/out" + depth + ".txt");
        print(node, out, depth, depth);
        out.close();
    }

    private static <T extends Geometry> void print(Node<Integer, Box> node, PrintStream out,
                                                   int minDepth, int maxDepth) {
        print(node, out, 0, minDepth, maxDepth);
    }

    private static <T extends Geometry> void print(Node<Integer, Box> node, PrintStream out, int depth,
                                                   int minDepth, int maxDepth) {
        if (depth > maxDepth) {
            return;
        }
        if (node instanceof NonLeaf) {
            NonLeaf<Integer, Box> n = (NonLeaf<Integer, Box>) node;
            Box b = node.geometry().mbb();
            if (depth >= minDepth)
                print(b, out);
            for (Node<Integer, Box> child : n.children()) {
                print(child, out, depth + 1, minDepth, maxDepth);
            }
        } else if (node instanceof Leaf && depth >= minDepth) {
            Leaf<Integer, Box> n = (Leaf<Integer, Box>) node;
            print(n.geometry().mbb(), out);
        }
    }

    private static void print(Box b, PrintStream out) {
        out.format("%s,%s,%s,%s,%s,%s\n", b.x1(), b.y1(), b.z1(), b.x2(), b.y2(), b.z2());
    }

    public static void main(String[] arg) throws IOException {

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMem = runtime.totalMemory() - runtime.freeMemory();

        long start = System.nanoTime();

        DataGen dataGen = new DataGen();
        dataGen.genAllData(DataGenConstant.dataType, DataGenConstant.divisionType);

        GenTopology genTopology = new GenTopology();
        genTopology.genTopology();

        final Box bounds = Box.create(0.0, 0.0, 0.0, 10000.0, 10000.0, 0.5);

        // create RTree
        int maxChildren = 10;
        RTree<Integer, Box> tree = RTree.star().minChildren((maxChildren) / 2)
                .maxChildren(maxChildren).bounds(bounds).create();

//	    loadData();

        for (int i = 0; i < IndoorSpace.iPartitions.size(); i++) {
            Partition partition = IndoorSpace.iPartitions.get(i);
            TreeNode node = new TreeNode(partition.getX1(), partition.getY1(), (partition.getmFloor()) * 0.1
                    , partition.getX2(), partition.getY2(), (partition.getmFloor() + 1) * 0.1, partition,
                    partition.getdistMatrix(), partition.getTopology());

            Box box = Box.create(partition.getX1(), partition.getY1(), (partition.getmFloor()) * 0.1
                    , partition.getX2(), partition.getY2(), (partition.getmFloor() + 1) * 0.1);
            box.setmNode(node);

            tree = tree.add(partition.getmID(), box);

//	    		if ((partition.getmFloor()) * 0.1 != ((partition.getmFloor() + 1) * 0.1) - 0.1) System.out.println("something wrong " + partition.getmID() + " " + partition.getmFloor());
        }

        long end = System.nanoTime();
        long endMem = runtime.totalMemory() - runtime.freeMemory();

        double time = (double) (end - start) / 1000 / 1000;
        double memory = (double) (endMem - startMem) / 1024 / 1024;
        System.out.println("time: " + time + " ms");
        System.out.println("memory: " + memory + " mb");
//
        ObjectGen objectGen = new ObjectGen();
        objectGen.readObjects(500);

        dk.aau.cs.indoorqueries.common.indoorEntitity.Point ps = new dk.aau.cs.indoorqueries.common.indoorEntitity.Point(20, 20, 0);
        dk.aau.cs.indoorqueries.common.indoorEntitity.Point pt = new dk.aau.cs.indoorqueries.common.indoorEntitity.Point(300, 700, 1);


        ICIndex_KNN icIndex_knn = new ICIndex_KNN();
        icIndex_knn.iKNN(ps, 3, tree);
    }
}
