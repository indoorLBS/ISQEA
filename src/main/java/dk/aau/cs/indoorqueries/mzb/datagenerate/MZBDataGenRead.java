package dk.aau.cs.indoorqueries.mzb.datagenerate;

import dk.aau.cs.indoorqueries.common.indoorEntitity.Door;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Floor;
import dk.aau.cs.indoorqueries.common.indoorEntitity.IndoorSpace;
import dk.aau.cs.indoorqueries.common.indoorEntitity.Partition;
import dk.aau.cs.indoorqueries.common.utilities.DataGenConstant;
import dk.aau.cs.indoorqueries.common.utilities.DoorType;
import dk.aau.cs.indoorqueries.common.utilities.RoomType;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

/**
 * read data
 * @author Tiantian Liu
 */
public class MZBDataGenRead {

    public static String inputDoor = System.getProperty("user.dir") + "/inputfiles/MZB/door_diType_" + DataGenConstant.divisionType + "_new.txt";
    public static String inputRoom = System.getProperty("user.dir") + "/inputfiles/MZB/room_diType_" + DataGenConstant.divisionType + "_new.txt";

    public void dataGen() throws IOException {

        // load the data generation constants of MZB dataset
        dk.aau.cs.indoorqueries.common.utilities.DataGenConstant.init("mzb");

        Path path1 = Paths.get(inputRoom);
        Scanner scanner1 = new Scanner(path1);

        while (scanner1.hasNextLine()) {
            String line = scanner1.nextLine();
            String[] tempArr = line.split(" ");
            int roomId = Integer.parseInt(tempArr[0]);
//            System.out.println("roomId: " + roomId);
            int roomType = Integer.parseInt(tempArr[1]);

//            if (tempArr[1].equals("ROOM")) {
////                System.out.println("room");
//                roomType = RoomType.STORE;
//            }
//            else if (tempArr[1].equals("HALLWAY")) {
//                roomType = RoomType.HALLWAY;
//            }
//            else {
//                roomType = RoomType.STAIRCASE;
//            }

            double x1 = Double.parseDouble(tempArr[2]);
            double y1 = Double.parseDouble(tempArr[3]);
            double x2 = Double.parseDouble(tempArr[4]);
            double y2 = Double.parseDouble(tempArr[5]);
            int floor = Integer.parseInt(tempArr[6]);
            String[] doorArr = tempArr[7].split(",");

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

            for (int i = 0; i < doorArr.length; i++) {
                int doorId = Integer.parseInt(doorArr[i]);
                par.addDoor(doorId);
            }
            IndoorSpace.iPartitions.add(par);
        }

        Path path2 = Paths.get(inputDoor);
        Scanner scanner2 = new Scanner(path2);

        while (scanner2.hasNextLine()) {
            String line = scanner2.nextLine();
            String[] tempArr = line.split(" ");
            int doorId = Integer.parseInt(tempArr[0]);
            int room1 = Integer.parseInt(tempArr[1]);
            int room2 = Integer.parseInt(tempArr[2]);
            double x = Double.parseDouble(tempArr[3]);
            double y = Double.parseDouble(tempArr[4]);
            int floor = Integer.parseInt(tempArr[6]);
            Door door = new Door(x, y);
            door.setmFloor(floor);

            door.setmPartitions(new ArrayList<Integer>(Arrays.asList(room1, room2)));
            IndoorSpace.iDoors.add(door);

        }

        for (int i = 0; i < 17; i++) {
            Floor floor = new Floor(i);
            for (int j = 0; j < IndoorSpace.iPartitions.size(); j++) {
                Partition par = IndoorSpace.iPartitions.get(j);
                if (par.getmFloor() == i) {
                    floor.addPartition(par.getmID());
                }
            }
            IndoorSpace.iFloors.add(floor);
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
        int floorNum = 2;

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
            ArrayList<Integer> doors = new ArrayList<>();
            for (int i = 0; i < IndoorSpace.iFloors.get(floorNum).getmPartitions().size(); i++) {
                ArrayList<Integer> partitions = IndoorSpace.iFloors.get(floorNum).getmPartitions();
                for (int j = 0; j < partitions.size(); j++) {
                    Partition par = IndoorSpace.iPartitions.get(partitions.get(j));
                    ArrayList<Integer> conDoors = par.getmDoors();
                    for (int k = 0; k < conDoors.size(); k++) {
                        if (!doors.contains(conDoors.get(k)) && IndoorSpace.iDoors.get(conDoors.get(k)).getmFloor() == floorNum) {
                            doors.add(conDoors.get(k));
                        }
                    }
                }
            }
            Iterator<Integer> itr2 = doors.iterator();
            while (itr2.hasNext()) {
                int curDoorId = itr2.next();
                Door curDoor = IndoorSpace.iDoors.get(curDoorId);
//                if (curDoor.getmID() >= 299) break;
                g.fillOval((int) (curDoor.getX() * DataGenConstant.zoomLevel),
                        (int) (curDoor.getY() * DataGenConstant.zoomLevel), 6,
                        6);
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
                if (curPartition.getmFloor() != floorNum) continue;
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
                        (int) (curPartition.getX1() * DataGenConstant.zoomLevel),
                        (int) (curPartition.getY1() * DataGenConstant.zoomLevel),
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

    public static void main(String args[]) throws IOException {
        MZBDataGenRead dataGen = new MZBDataGenRead();
        dataGen.dataGen();
        dataGen.drawFloor();
    }
}
