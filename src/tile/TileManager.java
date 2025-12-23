package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class TileManager {

    GamePanel gp;
    public Tile[]  tile;
    public int[][] mapTileNum;


    public TileManager(GamePanel gp){

        this.gp = gp;

        tile = new Tile[999];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap("/maps/Calamba.txt");
    }

    public void getTileImage(){
        // keep using UtilityTool where appropriate, but use our nearest-neighbor scaler below to prevent bleeding
        setup(101, "grass", false);
        setup(102, "pathway", false);

        setup(103, "bench1", true);
        setup(104, "bench2", true);
        setup(105, "bonfire", true);
//        setup(106, "wowoConcha_grave", true);

        setup(107, "path_1", false);
        setup(108, "path_2", false);
        setup(109, "path_3", false);
        setup(110, "path_4", false);

        setup(111, "path_5", false);
        setup(112, "path_6", false);
        setup(113, "path_7", false);
        setup(114, "path_8", false);


        setup(115, "stone", true);
        setup(116, "stonegrass", true);
        setup(117, "brickwall", true);


        setup(118, "water", true);
        setup(119, "well_0", true);
        setup(120, "well_1", true);
        setup(121, "well_2", true);
        setup(122, "well_3", true);
        setup(123, "well_4", true);
        setup(124, "well_5", true);
        setup(125, "wowoplot", false);
        setup(126, "log_4", true);


        setup(127, "bush_0", true);
        setup(128, "wowobush2", true);
        //setup(129, "", true);
        //setup(130, "", true);
        //setup(131, "", true);
        //setup(132, "", true);

        setup(133, "rock00", true);
        setup(134, "rock01", true);


        setup(135, "sand", false);

        setup(136, "sand_top", true);
        setup(137, "sand_bottom", true);
        setup(138, "sand_left", true);
        setup(139, "sand_right", true);


        setup(140, "sand_topleft", true);
        setup(141, "sand_topright", true);
        setup(142, "sand_bottomleft", true);
        setup(143, "sand_bottomright", true);

        setup(144, "sand_middle1", true);
        setup(145, "sand_middle2", true);
        setup(146, "sand_middle3", true);
        setup(147, "sand_middle4", true);

        setup(148, "sand_grass_top", false);
        setup(149, "sand_grass_bottom", false);
        setup(150, "sand_grass_left", false);
        setup(151, "sand_grass_right", false);

        setup(152, "sand_grass_topleft", false);
        setup(153, "sand_grass_topright", false);
        setup(154, "sand_grass_bottomleft", false);
        setup(155, "sand_grass_bottomright", false);

        setup(156, "sand_grass_middle1", false);
        setup(157, "sand_grass_middle2", false);
        setup(158, "sand_grass_middle3", false);
        setup(159, "sand_grass_middle4", false);

        setup(160, "tree1_0", true);
        setup(161, "tree1_1", true);
        setup(162, "tree1_2", true);
        setup(163, "tree1_3", true);
        setup(164, "tree1_4", true);
        setup(165, "tree1_5", true);

        setup(166, "tree2_0", true);
        setup(167, "tree2_1", true);
        setup(168, "tree2_2", true);
        setup(169, "tree2_3", true);
        setup(170, "tree2_4", true);
        setup(171, "tree2_5", true);

        setup(172, "wowo_statue0", true);
        setup(173, "wowo_statue1", true);
        setup(174, "wowo_statue2", true);
        setup(175, "wowo_statue3", true);

        setup(176, "floor_house", false);

        setup(177, "pillartopright_1", true);
        setup(178, "pillartopright_2", true);
        setup(179, "pillartopright_3", true);

        setup(180, "pillartopleft_1", true);
        setup(181, "pillartopleft_2", true);
        setup(182, "pillartopleft_3", true);

        setup(183, "pillarbottomright_1", true);
        setup(184, "pillarbottomright_2", true);
        setup(185, "pillarbottomright_3", true);

        setup(186, "pillarbottomleft_1", true);
        setup(187, "pillarbottomleft_2", true);
        setup(188, "pillarbottomleft_3", false);

        setup(189, "pillartop_1", true);
        setup(190, "pillartop_2", false);
        setup(191, "pillarbottom_1", true);
        setup(192, "pillarbottom_2", false);
        setup(193, "pillar_wall", true);
        setup(194, "pillarright", true);
        setup(195, "pillarleft", true);

        setup(196, "pillarmiddle_1", true);
        setup(197, "pillarmiddle_2", true);
        setup(198, "pillarmiddle_3", false);

        setup(199, "pillarmiddle_4", true);
        setup(200, "pillarmiddle_5", true);
        setup(201, "pillarmiddle_6", true);

        setup(202, "pillarmiddle_7", false);
        setup(203, "pillarmiddle_8", false);
        setup(204, "pillarmiddle_9", false);
        setup(205, "pillarmiddle_10", false);

        //setup(206, "", false);
        // setup(207, "", true);

        setup(208, "window1_1", true);
        setup(209, "window1_2", true);

        setup(210, "bed1_1", true);
        setup(211, "bed1_2", false);
        setup(212, "bed1_3", true);
        setup(213, "bed1_4", false);

        setup(214, "bed2_1", true);
        setup(215, "bed2_2", true);
        setup(216, "bed2_3", true);
        setup(217, "bed2_4", true);

        setup(218, "divider_1", true);
        setup(219, "divider_2", true);
        setup(220, "divider_3", false);

        setup(221, "pillarmiddoor_1", false);
        setup(222, "pillarmiddoor_2", true);
        setup(223, "pillarmiddoor_3", false);

        setup(224, "grasscorner_bottomleft", false);
        setup(225, "grasscorner_bottomright", false);
        setup(226, "grasscorner_topleft", false);
        setup(227, "grasscorner_topright", false);

        setup(228, "fence1", true);
        setup(229, "fence2", true);
        setup(230, "fence3", true);
        setup(231, "fence4", true);
        setup(232, "fence5", true);
        setup(233, "fence6", true);
        setup(234, "fence7", true);
        setup(235, "fence8", true);

        setup(236, "divider_4", true);
        setup(237, "divider_5", true);
        setup(238, "divider_6", true);
        setup(239, "divider_7", true);

        setup(240, "shelf_1", true);
        setup(241, "rug_1", false);
        setup(242, "rug_2", false);
        setup(243, "rug_3", false);
        setup(244, "rug_4", false);
        setup(245, "rug_5", false);
        setup(246, "rug_6", false);
        setup(247, "rug_7", false);
        setup(248, "bed3_1", true);
        setup(249, "bed3_2", true);
        setup(250, "bed3_3", false);
        setup(251, "bed3_4", false);

        setup(252, "bed4_1", true);
        setup(253, "bed4_2", true);
        setup(254, "bed4_3", false);
        setup(255, "bed4_4", false);

        setup(256, "wall_girl_1", true);
        setup(257, "wall_girl_2", true);
        setup(258, "wall_girl_3", false);
        setup(259, "wall_girl_4", false);

        setup(260, "pillarmiddoor_4", true);
        setup(261, "pillarmiddoor_5", true);
        setup(262, "pillarmiddoor_6", false);

        setup(263, "pond", true);

        setup(264, "table1_1", false);
        setup(265, "table1_2", false);
        setup(266, "table1_3", true);
        setup(267, "table1_4", true);

        setup(268, "fountain00", true);
        setup(269, "fountain01", true);
        setup(270, "fountain02", true);
        setup(271, "fountain03", true);
        setup(272, "fountain04", true);
        setup(273, "fountain05", true);
        setup(274, "fountain06", true);
        setup(275, "fountain07", true);
        setup(276, "fountain08", true);
        setup(277, "fountain09", true);
        setup(278, "fountain10", true);
        setup(279, "fountain11", true);
        setup(280, "fountain12", true);
        setup(281, "fountain13", true);
        setup(282, "fountain14", true);
        setup(283, "fountain15", true);

        //setup(284, "", false);
        //setup(285, "", false);
        //setup(286, "", false);
        //setup(287, "", false);
        //setup(288, "", false);
        //setup(289, "", false);
        //setup(290, "", false);
        //setup(291, "", false);
        //setup(292, "", false);
        //setup(293, "", false);
        //setup(294, "", false);
        //setup(295, "", false);
        //setup(296, "", false);
        //setup(297, "", false);
        //setup(298, "", false);
        //setup(299, "", false);
        //setup(300, "", false);
        //setup(301, "", false);

        setup(302, "pillarbotcorner_1", false);
        setup(303, "pillarbotcorner_2", false);
        setup(304, "pillarbotcorner_3", false);

        //setup(305, "", false);
        //setup(306, "", true);
        //setup(307, "", true);
        //setup(308, "", true);
        //setup(309, "", true);
        //setup(310, "", true);
        //setup(311, "", true);
        //setup(312, "", true);
        //setup(313, "", true);
        //setup(314, "", true);
        //setup(315, "", true);
        //setup(316, "", true);
        //setup(317, "", true);
        //setup(318, "", true);
        //setup(319, "", true);
        //setup(320, "", true);
        //setup(321, "", true);
        //setup(322, "", true);
        //setup(323, "", true);
        //setup(324, "", true);

        setup(325, "house300", true);
        setup(326, "house301", true);
        setup(327, "house302", true);
        setup(328, "house303", true);
        setup(329, "house304", true);
        setup(330, "house305", true);
        setup(331, "house306", true);
        setup(332, "house307", true);
        setup(333, "house308", true);
        setup(334, "house309", true);
        setup(335, "house310", true);
        setup(336, "house311", true);
        setup(337, "house312", true);
        setup(338, "house313", true);
        setup(339, "house314", true);
        setup(340, "house315", true);
        setup(341, "house316", true);
        setup(342, "house317", true);
        setup(343, "house318", true);
        setup(344, "house319", true);
        setup(345, "house320", true);
        setup(346, "house321", true);
        setup(347, "house322", true);
        setup(348, "house323", true);
        setup(349, "house324", true);

        setup(350, "rock200", true);
        setup(351, "rock201", true);
        setup(352, "rock202", true);
        setup(353, "rock203", true);
        setup(354, "rock204", true);
        setup(355, "rock205", true);
        setup(356, "rock206", true);
        setup(357, "rock207", true);
        setup(358, "rock208", true);
        setup(359, "rock209", true);
        setup(360, "rock210", true);
        setup(361, "rock211", true);
        setup(362, "rock212", true);
        setup(363, "rock213", true);
        setup(364, "rock214", true);
        setup(365, "rock215", true);


        setup(366, "pondedge_0", false);
        setup(367, "pondedge_1", false);
        setup(368, "pondedge_2", false);
        setup(369, "pondedge_3", false);
        setup(370, "pondedge_5", false);
        setup(371, "pondedge_6", false);
        setup(372, "pondedge_7", false);
        setup(373, "pondedge_8", false);

        setup(374, "pondedge_4.1", false);
        setup(375, "pondedge_4.2", false);
        setup(376, "pondedge_4.3", false);
        setup(377, "pondedge_4.4", false);

        setup(378, "rock300", true);
        setup(379, "rock301", true);
        setup(380, "rock302", true);
        setup(381, "rock303", true);
        setup(382, "rock304", true);
        setup(383, "rock305", true);
        setup(384, "rock306", true);
        setup(385, "rock307", true);
        setup(386, "rock308", true);
        setup(387, "rock309", true);
        setup(388, "rock310", true);
        setup(389, "rock311", true);
        setup(390, "rock312", true);
        setup(391, "rock313", true);
        setup(392, "rock314", true);

        setup(393, "furniture1_0", true);
        setup(394, "furniture1_1", true);
        setup(395, "furniture1_2", true);
        setup(396, "furniture1_3", true);
        setup(397, "furniture1_4", true);

        setup(398, "furniture1_5", true);
        setup(399, "furniture1_6", false);
        setup(400, "furniture1_7", false);
        setup(401, "furniture1_8", false);

        setup(402, "furniture1_9", true);
        setup(403, "furniture1_10", false);
        setup(404, "furniture1_11", true);
        setup(405, "furniture1_12", false);
        setup(406, "furniture1_13", true);
        setup(407, "furniture1_14", false);

        setup(408, "furniture1_15", true);
        setup(409, "furniture1_16", false);
        setup(410, "furniture1_17", true);
        setup(411, "furniture1_18", false);
        setup(412, "furniture1_19", true);
        setup(413, "furniture1_20", false);

        setup(414, "dining_1", true);
        setup(415, "dining_2", false);
        setup(416, "dining_3", true);
        setup(417, "dining_4", false);
        setup(418, "dining_5", true);
        setup(419, "dining_6", false);
        setup(420, "dining_7", true);
        setup(421, "dining_8", false);
        setup(422, "dining_9", true);
        setup(423, "dining_10", false);
        setup(424, "dining_11", true);
        setup(425, "dining_12", false);
        setup(426, "dining_13", true);
        setup(427, "dining_14", false);
        setup(428, "dining_15", true);
        setup(429, "dining_16", false);
        setup(430, "dining_17", false);
        setup(431, "dining_18", false);
        setup(432, "dining_19", false);
        setup(433, "dining_20", false);
        setup(434, "dining_21", false);
        setup(435, "dining_22", false);
        setup(436, "dining_23", false);
        setup(437, "dining_24", false);
        setup(438, "dining_25", false);
        setup(439, "dining_26", false);

        setup(440, "rug4_1", false);
        setup(441, "rug4_2", false);
        setup(442, "rug4_3", false);
        setup(443, "rug4_4", false);
        setup(444, "rug4_5", false);
        setup(445, "rug4_6", false);

        setup(446, "pillarmid_solo1", true);
        setup(447, "pillarmid_solo2", true);
        setup(448, "pillarmid_solo3", false);

        setup(449, "pillarmid_solo4", true);
        setup(450, "pillarmid_solo5", true);
        setup(451, "pillarmid_solo6", false);

        setup(452, "pillarbot_cross1", true);
        setup(453, "pillarbot_cross2", true);
        setup(454, "pillarbot_cross3", true);
        setup(455, "pillarbot_cross4", true);
        setup(456, "pillarbot_cross5", true);

        setup(457, "rug5_0", false);
        setup(458, "rug5_1", false);
        setup(459, "rug5_2", false);
        setup(460, "rug5_3", false);
        setup(461, "rug5_4", false);
        setup(462, "rug5_5", false);
        setup(463, "rug5_6", false);
        setup(464, "rug5_7", false);
        setup(465, "rug5_8", false);

        setup(466, "bed5_1", true);
        setup(467, "bed5_2", true);
        setup(468, "bed5_3", true);
        setup(469, "bed5_4", true);
        setup(470, "bed5_5", true);
        setup(471, "bed5_6", true);
        setup(472, "bed5_7", false);
        setup(473, "bed5_8", false);
        setup(474, "bed5_9", false);

        setup(475, "pot1_1", true);
        setup(476, "pot2_1", true);
        setup(477, "pot2_2", true);

        setup(478, "bed6_1", true);
        setup(479, "bed6_2", true);
        setup(480, "bed6_3", true);
        setup(481, "bed6_4", true);

        setup(482, "bed7_1", true);
        setup(483, "bed7_2", true);
        setup(484, "bed7_3", false);
        setup(485, "bed7_4", false);

        setup(486, "rug9_1", false);
        setup(487, "rug9_2", false);
        setup(488, "rug9_3", false);
        setup(489, "rug9_4", false);
        setup(490, "rug9_5", false);
        setup(491, "rug9_6", false);
        setup(492, "rug9_7", false);
        setup(493, "rug9_8", false);
        setup(494, "rug9_9", false);

        setup(495, "sofa_00", true);
        setup(496, "sofa_01", true);
        setup(497, "sofa_02", true);
        setup(498, "sofa_03", true);
        setup(499, "sofa_04", true);
        setup(500, "sofa_05", false);
        setup(501, "sofa_06", true);
        setup(502, "sofa_07", true);
        setup(503, "sofa_08", false);
        setup(504, "sofa_09", true);
        setup(505, "sofa_10", false);
        setup(506, "sofa_11", true);
        setup(507, "sofa_12", true);
        setup(508, "sofa_13", false);
        setup(509, "sofa_14", true);
        setup(510, "sofa_15", false);
        setup(511, "sofa_16", false);
        setup(512, "sofa_17", false);
        setup(513, "sofa_18", false);
        setup(514, "sofa_19", false);
        setup(515, "sofa_20", false);
        setup(516, "sofa_21", false);
        setup(517, "rug10_1", false);
        setup(518, "rug10_2", false);

        setup(519, "pillarside_stair_1", false);
        setup(520, "pillarside_stair_2", false);
        setup(521, "study_room_00", true);
        setup(522, "study_room_01", true);
        setup(523, "study_room_02", true);
        setup(524, "study_room_03", true);
        setup(525, "study_room_04", true);

        setup(526, "study_room_05", false);
        setup(527, "study_room_06", false);
        setup(528, "study_room_07", false);
        setup(529, "study_room_08", false);
        setup(530, "study_room_09", false);

        setup(531, "study_room_10", false);
        setup(532, "study_room_11", false);
        setup(533, "study_room_12", false);
        setup(534, "study_room_13", true);

        setup(535, "study_room_14", false);
        setup(536, "study_room_15", false);
        setup(537, "study_room_16", false);
        setup(538, "study_room_17", true);
        setup(539, "study_room_18", true);
        setup(540, "study_room_19", true);

        setup(541, "study_room_20", false);
        setup(542, "study_room_21", false);
        setup(543, "study_room_22", true);
        setup(544, "study_room_23", true);
        setup(545, "study_room_24", true);

        setup(546, "npc_house100", false);
        setup(547, "npc_house101", false);
        setup(548, "npc_house102", false);
        setup(549, "npc_house103", false);
        setup(550, "npc_house104", false);

        setup(551, "npc_house105", false);
        setup(552, "npc_house106", false);
        setup(553, "npc_house107", false);
        setup(554, "npc_house108", false);
        setup(555, "npc_house109", false);

        setup(556, "npc_house110", false);
        setup(557, "npc_house111", false);
        setup(558, "npc_house112", false);
        setup(559, "npc_house113", false);
        setup(560, "npc_house114", false);

        setup(561, "npc_house115", false);
        setup(562, "npc_house116", false);
        setup(563, "npc_house117", false);
        setup(564, "npc_house118", false);
        setup(565, "npc_house119", false);

        setup(566, "npc_house120", false);
        setup(567, "npc_house121", false);
        setup(568, "npc_house122", false);
        setup(569, "npc_house123", false);
        setup(570, "npc_house124", false);
    }

    public void setup(int index, String imagePath, boolean collision){
        UtilityTool uTool = new UtilityTool();

        try {
            tile[index] = new Tile();
            BufferedImage raw = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imagePath + ".png"));
            // Scale using nearest-neighbor to avoid bleeding for pixel-art tiles
            tile[index].image = scaleTileImage(raw, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;

        }catch (IOException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    // Nearest-neighbor scaling for pixel-art (prevents blending/bleeding between tiles)
    private BufferedImage scaleTileImage(BufferedImage src, int width, int height) {
        if (src == null) return null;
        BufferedImage dest = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = dest.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.drawImage(src, 0, 0, width, height, null);
        g.dispose();
        return dest;
    }

    //GENERATES THE TILE MAP
    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            if (is == null) {
                System.out.println("Map file not found: " + filePath);
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < gp.maxWorldRow) {
                String[] numbers = line.trim().split("\\s+");
                for (int col = 0; col < numbers.length && col < gp.maxWorldCol; col++) {
                    try {
                        mapTileNum[col][row] = Integer.parseInt(numbers[col]);
                    } catch (NumberFormatException nfe) {
                        System.out.println("Invalid number at row " + row + " col " + col + ": '" + numbers[col] + "'");
                        mapTileNum[col][row] = 0; // fallback
                    }
                }
                row++;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void draw(Graphics2D g2){

        // Draw using nested loops for clarity and correctness
        for (int worldRow = 0; worldRow < gp.maxWorldRow; worldRow++) {
            for (int worldCol = 0; worldCol < gp.maxWorldCol; worldCol++) {

                int tileNum = mapTileNum[worldCol][worldRow];

                // bounds check
                if (tileNum < 0 || tileNum >= tile.length) {
                    continue;
                }

                int worldX = worldCol * gp.tileSize;
                int worldY = worldRow * gp.tileSize;
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;

                // only draw tiles in the view rectangle (same logic as before)
                if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                        worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                        worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                        worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

                    if (tile[tileNum] != null && tile[tileNum].image != null) {
                        // draw at integer coordinates; image already scaled to tileSize
                        g2.drawImage(tile[tileNum].image, screenX, screenY, null);
                    } else {
                        // optional: draw nothing or a fallback; don't crash if image missing
                        // g2.setColor(Color.MAGENTA);
                        // g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
                    }
                }
            }
        }
    }
}