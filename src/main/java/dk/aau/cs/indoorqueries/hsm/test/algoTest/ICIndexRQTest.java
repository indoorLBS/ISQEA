package dk.aau.cs.indoorqueries.hsm.test.algoTest;

import com.github.davidmoten.rtree3d.*;
import com.github.davidmoten.rtree3d.geometry.Box;
import com.github.davidmoten.rtree3d.geometry.Geometry;
import com.github.davidmoten.rtree3d.geometry.Point;
import dk.aau.cs.indoorqueries.common.algorithm.ICIndex_RQ;
import dk.aau.cs.indoorqueries.common.iDModel.GenTopology;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorObject;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Partition;
import dk.aau.cs.indoorqueries.common.rStarTree3D.TreeNode;
import dk.aau.cs.indoorqueries.hsm.datagenerate.HSMDataGenRead;
import dk.aau.cs.indoorqueries.hsm.datagenerate.ObjectGen;
import rx.Observable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * test RQ using ICIndex
 * @author Tiantian Liu
 */
public class ICIndexRQTest {
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
        HSMDataGenRead dateGenReadMen = new HSMDataGenRead();
        dateGenReadMen.dataGen();

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

        System.out.println("tree size = " + tree.size());

//
        String fileInput = System.getProperty("user.dir") + "/source.r";
        Runtime.getRuntime().exec("/bin/sh R CMD BATCH " + fileInput);
        System.out.println("png");

        ObjectGen objectGen = new ObjectGen();
        objectGen.readObjects(1500);

        dk.aau.cs.indoorqueries.common.indoorEntitity.Point ps = new dk.aau.cs.indoorqueries.common.indoorEntitity.Point(1450, 1650, 0);
        dk.aau.cs.indoorqueries.common.indoorEntitity.Point pt = new dk.aau.cs.indoorqueries.common.indoorEntitity.Point(15, 25, 13);


        ICIndex_RQ icIndex_rq = new ICIndex_RQ();
        icIndex_rq.iRQ(ps, 100, tree);
    }
}
