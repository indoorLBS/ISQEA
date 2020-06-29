package dk.aau.cs.indoorqueries.hsm.datagenerate;

import dk.aau.cs.indoorqueries.common.indoorEntitity.Door;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Floor;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Partition;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.common.utilities.RoomType;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class HSMDataGenRead {

    public static String inputDoor = System.getProperty("user.dir") + "/inputfiles/HSM/Door.txt";
    public static String inputRoom = System.getProperty("user.dir") + "/inputfiles/HSM/Par.txt";

    public static void main(String[] args) throws IOException {
        HSMDataGenRead dataGen = new HSMDataGenRead();
        dataGen.dataGen();

        dataGen.drawFloor();
    }

    public void dataGen() throws IOException {

        // load the data generation constants of HSM dataset
        dk.aau.cs.indoorqueries.common.utilities.DataGenConstant.init("hsm");

        Path path1 = Paths.get(inputRoom);
        Scanner scanner1 = new Scanner(path1);

        while (scanner1.hasNextLine()) {
            String line = scanner1.nextLine();
            String[] tempArr = line.split("\t");
            int roomId = Integer.parseInt(tempArr[0]);

            double x1 = Double.parseDouble(tempArr[1]);
            double y1 = Double.parseDouble(tempArr[3]);
            double x2 = Double.parseDouble(tempArr[2]);
            double y2 = Double.parseDouble(tempArr[4]);
            int floor = Integer.parseInt(tempArr[5]);
            int roomType = Integer.parseInt(tempArr[6].split(" ")[0]);

            if (x1 > x2) {
                double temp = x2;
                x2 = x1;
                x1 = temp;
            }

            if (y1 > y2) {
                double temp = y2;
                y2 = y1;
                y1 = temp;
            }

            Partition par = new Partition(x1, x2, y1, y2, roomType);
            par.setmFloor(floor);

            for (int i = 9; i < tempArr.length; i++) {
                int doorId = Integer.parseInt(tempArr[i]);
                par.addDoor(doorId);
            }
            IndoorSpace.iPartitions.add(par);
        }

        Path path2 = Paths.get(inputDoor);
        Scanner scanner2 = new Scanner(path2);

        while (scanner2.hasNextLine()) {
            String line = scanner2.nextLine();
            String[] tempArr = line.split("\t");
            int doorId = Integer.parseInt(tempArr[0]);
//            System.out.println("doorId: " + doorId);
            double x = Double.parseDouble(tempArr[1]);
            double y = Double.parseDouble(tempArr[2]);
            int floor = Integer.parseInt(tempArr[3]);
            Door door = new Door(x, y);
            ArrayList<Integer> rooms = new ArrayList<>();
            for (int i = 7; i < tempArr.length; i++) {
                rooms.add(Integer.parseInt(tempArr[i]));
            }
            door.setmPartitions(rooms);
            door.setmFloor(floor);

            IndoorSpace.iDoors.add(door);

        }

        for (int i = 0; i < DataGenConstant.nFloor; i++) {
            Floor floor = new Floor(i);
            for (int j = 0; j < IndoorSpace.iPartitions.size(); j++) {
                Partition par = IndoorSpace.iPartitions.get(j);
                if (par.getmFloor() == i) {
                    floor.addPartition(par.getmID());
                }
            }
            IndoorSpace.iFloors.add(floor);
        }

        for (int i = 0; i < IndoorSpace.iDoors.size(); i++) {
            Door door = IndoorSpace.iDoors.get(i);
            ArrayList<Integer> parIds = door.getmPartitions();
            for (int j = 0; j < parIds.size(); j++) {
                Partition par = IndoorSpace.iPartitions.get(parIds.get(j));
                if (!par.getmDoors().contains(door.getmID())) {
                    par.addDoor(door.getmID());
                }
            }
        }
    }

    /**
     * draws one single floor
     */
    public void drawFloor() {
        PaintingPanel pp = new PaintingPanel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(pp);
        frame.setBounds(0, 0, (int) ((DataGenConstant.floorRangeX + 3)
                        * DataGenConstant.zoomLevel),
                (int) ((DataGenConstant.floorRangeY + 3)
                        * DataGenConstant.zoomLevel));
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setResizable(true);
        frame.setVisible(true);
    }

    class PaintingPanel extends JPanel {

        private static final long serialVersionUID = 6050646791177968917L;

        /**
         * paint function, paint the indoor space with a zoom-level parameter
         * 1.paint the partitions(black-line & yellow BG color) 2.paint the
         * doors(red) 3.paint the indoor objects(blue)
         *
         * @param
         * @return
         * @throws
         */
        public void paint(Graphics g) {
            super.paint(g);
            paintPartitions(g);
            paintDoors(g);
//			paintIdrObjs(g);
        }

//		/**
//		 * paint indoor objects
//		 *
//		 * @return
//		 * @param g
//		 *            Graphics
//		 * @exception
//		 */
//		private void paintIdrObjs(Graphics g) {
//			// TODO Auto-generated method stub
//			g.setColor(Color.BLUE);
//			Iterator<IdrObj> itr3 = IndoorSpace.gIdrObjs.iterator();
//			while (itr3.hasNext()) {
//				IdrObj curIdrObj = itr3.next();
//				g.fillOval(
//						(int) (curIdrObj.getmTruePos().getX() * HSMDataGenConstant.zoomLevel),
//						(int) (curIdrObj.getmTruePos().getY() * HSMDataGenConstant.zoomLevel),
//						5, 5);
//			}
//		}

        /**
         * paint doors
         *
         * @param g Graphics
         * @return
         * @throws
         */
        private void paintDoors(Graphics g) {
            // TODO Auto-generated method stub
            g.setColor(Color.RED);
            Iterator<Door> itr2 = IndoorSpace.iDoors.iterator();
            while (itr2.hasNext()) {
                Door curDoor = itr2.next();
//                if (curDoor.getmID() >= 299) break;
                g.fillOval((int) (curDoor.getX() * DataGenConstant.zoomLevel),
                        (int) (curDoor.getY() * DataGenConstant.zoomLevel), 4,
                        4);
//                g.drawString(String.valueOf(curDoor.getmID()),
//                        (int) (curDoor.getX() * HSMDataGenConstant.zoomLevel),
//                        (int) (curDoor.getY() * HSMDataGenConstant.zoomLevel));
            }
        }

        /**
         * paint partitions
         *
         * @param g Graphics
         * @return
         * @throws
         */
        private void paintPartitions(Graphics g) {
            // TODO Auto-generated method stub

//			// four outdoor rectangles (is used in the is inBuilding function)
//			g.setColor(Color.WHITE);
//			g.fillRect(((int) HSMDataGenConstant.outRectTop.getX1())
//							* HSMDataGenConstant.zoomLevel,
//					((int) HSMDataGenConstant.outRectTop.getY1())
//							* HSMDataGenConstant.zoomLevel,
//					(int) HSMDataGenConstant.outRectTop.getWidth()
//							* HSMDataGenConstant.zoomLevel,
//					(int) HSMDataGenConstant.outRectTop.getHeight()
//							* HSMDataGenConstant.zoomLevel);
//			g.fillRect(((int) HSMDataGenConstant.outRectBottom.getX1())
//							* HSMDataGenConstant.zoomLevel,
//					((int) HSMDataGenConstant.outRectBottom.getY1())
//							* HSMDataGenConstant.zoomLevel,
//					(int) HSMDataGenConstant.outRectBottom.getWidth()
//							* HSMDataGenConstant.zoomLevel,
//					(int) HSMDataGenConstant.outRectBottom.getHeight()
//							* HSMDataGenConstant.zoomLevel);
//			g.fillRect(((int) HSMDataGenConstant.outRectLeft.getX1())
//							* HSMDataGenConstant.zoomLevel,
//					((int) HSMDataGenConstant.outRectLeft.getY1())
//							* HSMDataGenConstant.zoomLevel,
//					(int) HSMDataGenConstant.outRectLeft.getWidth()
//							* HSMDataGenConstant.zoomLevel,
//					(int) HSMDataGenConstant.outRectLeft.getHeight()
//							* HSMDataGenConstant.zoomLevel);
//			g.fillRect(((int) HSMDataGenConstant.outRectRight.getX1())
//							* HSMDataGenConstant.zoomLevel,
//					((int) HSMDataGenConstant.outRectRight.getY1())
//							* HSMDataGenConstant.zoomLevel,
//					(int) HSMDataGenConstant.outRectRight.getWidth()
//							* HSMDataGenConstant.zoomLevel,
//					(int) HSMDataGenConstant.outRectRight.getHeight()
//							* HSMDataGenConstant.zoomLevel);

            // all indoor partitions
            g.setColor(Color.BLACK);
            Iterator<Partition> itr = IndoorSpace.iPartitions.iterator();
            while (itr.hasNext()) {
                Partition curPartition = itr.next();
                if (curPartition.getmFloor() != 1) continue;
                if (curPartition.getmType() == RoomType.STORE) {
                    Color c = new Color(218, 227, 243);
                    g.setColor(c);
                }
                if (curPartition.getmType() == RoomType.HALLWAY) {
                    g.setColor(Color.WHITE);
                }
                if (curPartition.getmType() == RoomType.STAIRCASE) {
                    Color c = new Color(226, 240, 217);
                    g.setColor(c);
                }
                g.fillRect(
                        (int) ((curPartition.getX1() * DataGenConstant.zoomLevel)),
                        (int) ((curPartition.getY1() * DataGenConstant.zoomLevel)),
                        (int) ((curPartition.getX2() - curPartition.getX1())
                                * DataGenConstant.zoomLevel),
                        (int) ((curPartition.getY2() - curPartition.getY1())
                                * DataGenConstant.zoomLevel));
                g.setColor(Color.BLACK);
                g.drawRect(
                        (int) (curPartition.getX1() * DataGenConstant.zoomLevel),
                        (int) (curPartition.getY1() * DataGenConstant.zoomLevel),
                        (int) ((curPartition.getX2() - curPartition.getX1())
                                * DataGenConstant.zoomLevel),
                        (int) ((curPartition.getY2() - curPartition.getY1())
                                * DataGenConstant.zoomLevel));
//                g.drawString(String.valueOf(curPartition.getmID()),
//                        (int) ((curPartition.getX1() + curPartition.getX2()) / 2
//                                * HSMDataGenConstant.zoomLevel),
//                        (int) ((curPartition.getY1() + curPartition.getY2()) / 2
//                                * HSMDataGenConstant.zoomLevel));
            }
        }
    }
}
