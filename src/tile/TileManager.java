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
        loadMap("/maps/Chapter1.txt");
    }

    public void getTileImage(){
        setup(101, "grass", false);
        setup(102, "pathway", false);

        setup(103, "bench1", true);
        setup(104, "bench2", true);
        setup(105, "road", false);
        setup(106, "road1", false);

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


        setup(118, "water", false);
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
        setup(129, "road2", false);
        setup(130, "road3", false);
        setup(131, "road4", false);
        setup(132, "road5", false);

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
        setup(211, "bed1_2", true);
        setup(212, "bed1_3", true);
        setup(213, "bed1_4", true);

        setup(214, "bed2_1", true);
        setup(215, "bed2_2", true);
        setup(216, "bed2_3", true);
        setup(217, "bed2_4", true);

        setup(218, "divider_1", true);
        setup(219, "divider_2", true);
        setup(220, "divider_3", true);

        setup(221, "pillarmiddoor_1", true);
        setup(222, "pillarmiddoor_2", true);
        setup(223, "pillarmiddoor_3", true);

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
        setup(258, "wall_girl_3", true);
        setup(259, "wall_girl_4", true);

        setup(260, "pillarmiddoor_4", true);
        setup(261, "pillarmiddoor_5", true);
        setup(262, "pillarmiddoor_6", true);

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

        setup(284, "wooden_port_00", true);
        setup(285, "wooden_port_01", false);
        setup(286, "wooden_port_02", false);
        setup(287, "wooden_port_03", true);

        setup(288, "wooden_port_04", true);
        setup(289, "wooden_port_05", false);
        setup(290, "wooden_port_06", false);
        setup(291, "wooden_port_07", true);

        setup(292, "wooden_port_08", false);
        setup(293, "wooden_port_09", true);
        setup(294, "wooden_port_10", true);
        setup(295, "wooden_port_11", true);
        setup(296, "wooden_port_12", false);
        setup(297, "wooden_port_13", true);
        setup(298, "wooden_port_14", false);
        setup(299, "wooden_port_15", false);
        setup(300, "wooden_port_16", true);
        setup(301, "wooden_port_17", true);
        setup(324, "wooden_port_18", true);
        setup(206, "wooden_port_19", true);


        setup(302, "pillarbotcorner_1", true);
        setup(303, "pillarbotcorner_2", true);
        setup(304, "pillarbotcorner_3", true);

        setup(305, "edge00", false);
        setup(306, "edge01", true);
        setup(307, "edge02", true);
        setup(308, "edge03", true);
        setup(309, "edge04", true);
        setup(310, "edge05", true);
        setup(311, "edge06", true);
        setup(312, "edge07", true);
        setup(313, "edge08", true);
        setup(314, "edge09", true);
        setup(315, "edge10", true);
        setup(316, "edge11", true);
        setup(317, "edge12", true);
        setup(318, "edge13", true);
        setup(319, "edge14", true);

        setup(320, "circ_table0", true);
        setup(321, "circ_table1", true);
        setup(322, "circ_table2", true);
        setup(323, "circ_table3", true);


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


        setup(366, "pondedge_0", true);
        setup(367, "pondedge_1", true);
        setup(368, "pondedge_2", true);
        setup(369, "pondedge_3", true);
        setup(370, "pondedge_5", true);
        setup(371, "pondedge_6", true);
        setup(372, "pondedge_7", true);
        setup(373, "pondedge_8", true);

        setup(374, "pondedge_4.1", true);
        setup(375, "pondedge_4.2", true);
        setup(376, "pondedge_4.3", true);
        setup(377, "pondedge_4.4", true);

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
        setup(399, "furniture1_6", true);
        setup(400, "furniture1_7", true);
        setup(401, "furniture1_8", true);

        setup(402, "furniture1_9", true);
        setup(403, "furniture1_10", true);
        setup(404, "furniture1_11", true);
        setup(405, "furniture1_12", true);
        setup(406, "furniture1_13", true);
        setup(407, "furniture1_14", true);

        setup(408, "furniture1_15", true);
        setup(409, "furniture1_16", true);
        setup(410, "furniture1_17", true);
        setup(411, "furniture1_18", true);
        setup(412, "furniture1_19", true);
        setup(413, "furniture1_20", true);

        setup(414, "dining_1", false);
        setup(415, "dining_2", false);
        setup(416, "dining_3", true);
        setup(417, "dining_4", true);
        setup(418, "dining_5", false);
        setup(419, "dining_6", false);
        setup(420, "dining_7", true);
        setup(421, "dining_8", false);
        setup(422, "dining_9", true);
        setup(423, "dining_10", false);
        setup(424, "dining_11", true);
        setup(425, "dining_12", false);
        setup(426, "dining_13", false);
        setup(427, "dining_14", false);
        setup(428, "dining_15", true);
        setup(429, "dining_16", true);
        setup(430, "dining_17", true);
        setup(431, "dining_18", true);
        setup(432, "dining_19", true);
        setup(433, "dining_20", true);
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
        setup(448, "pillarmid_solo3", true);

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
        setup(472, "bed5_7", true);
        setup(473, "bed5_8", true);
        setup(474, "bed5_9", true);

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
        setup(499, "sofa_04", false);
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

        setup(526, "study_room_05", true);
        setup(527, "study_room_06", true);
        setup(528, "study_room_07", true);
        setup(529, "study_room_08", true);
        setup(530, "study_room_09", true);

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

        setup(571, "ateneo_floor", false);
        setup(572, "ateneo_window_1", true);
        setup(573, "ateneo_window_2", true);
        setup(574, "ateneo_window_3", true);
        setup(575, "ateneo_window_4",true);
        setup(576, "ateneo_walledge_1", true);
        setup(577, "ateneo_walledge_2", true);
        setup(578, "ateneo_wallcorner_1", true);
        setup(579, "ateneo_wallcorner_2", true);
        setup(580, "ateneo_wall_left", true);
        setup(581, "ateneo_wall_right", true);
        setup(582, "ateneo_wall_bottom", true);
        setup(583, "ateneo_wall_top", true);

        setup(584, "road_0", true);
        setup(585, "road_1", true);
        setup(586, "road_2", true);
        setup(587, "road_3", false);
        setup(588, "road_4", false);
        setup(589, "road_5", false);
        setup(590, "road_6", false);
        setup(591, "road_7", false);
        setup(592, "road_8", false);

        setup(592, "BRIDGE00", true);
        setup(593, "BRIDGE01", true);
        setup(594, "BRIDGE02", true);

        setup(595, "BRIDGE03", false);
        setup(596, "BRIDGE04", false);
        setup(597, "BRIDGE05", false);

        setup(598, "BRIDGE06", true);
        setup(599, "BRIDGE07", true);
        setup(600, "BRIDGE08", true);

        setup(601, "ateneo_bs1", true);
        setup(602, "ateneo_bs2", true);
        setup(603, "ateneo_bs3", true);
        setup(604, "ateneo_bs4", true);

        setup(605, "ateneo_plant1_1", true);
        setup(606, "ateneo_plant1_2", true);
        setup(607, "ateneo_plant2_1", true);
        setup(608, "ateneo_plant2_2", true);
        setup(609, "ateneo_plant3_1", true);
        setup(610, "ateneo_plant3_2", true);
        setup(611, "ateneo_plant4_1", true);
        setup(612, "ateneo_plant4_2", true);

        setup(613, "ateneo_painting1", true);

        setup(614, "ateneo_table1", true);
        setup(615, "ateneo_table2", true);
        setup(616, "ateneo_teacher1", true);
        setup(617, "ateneo_teacher2", true);

        setup(618, "mat_b0", false);
        setup(619, "mat_b1", false);
        setup(620, "mat_b2", false);
        setup(621, "mat_b3", false);
        setup(622, "mat_b4", false);
        setup(623, "mat_b5", false);

        setup(624, "mat2_b0", false);
        setup(625, "mat2_b1", false);
        setup(626, "mat2_b2", false);
        setup(627, "mat2_b3", false);
        setup(628, "mat2_b4", false);
        setup(629, "mat2_b5", false);
        setup(630, "mat2_b6", false);
        setup(631, "mat2_b7", false);
        setup(632, "mat2_b8", false);

        setup(633, "BRIDGE2_0", true);
        setup(634, "BRIDGE2_1", false);
        setup(635, "BRIDGE2_2", true);
        setup(636, "BRIDGE2_3", true);
        setup(637, "BRIDGE2_4", false);
        setup(638, "BRIDGE2_5", true);
        setup(639, "BRIDGE2_6", true);
        setup(640, "BRIDGE2_7", false);
        setup(641, "BRIDGE2_8", true);

        setup(642, "carpet_1", false);
        setup(643, "carpet_2", false);
        setup(644, "carpet_3", false);
        setup(645, "carpet_4", false);
        setup(646, "carpet_5", false);
        setup(647, "carpet_6", false);
        setup(648, "carpet_7", false);
        setup(649, "carpet_8", false);
        setup(650, "carpet_9", false);
        setup(651, "carpet_10", true);
        setup(652, "carpet_11", true);
        setup(653, "carpet_12", true);
        setup(654, "carpet_13", true);
        setup(655, "carpet_14", true);

        setup(656,"window2_1", true);
        setup(657,"window2_2", true);

        setup(658,"cell1", true);
        setup(659,"cell2", true);
        setup(660,"cell3", true);
        setup(661,"cell4", true);

        setup(662,"cellwall_1", true);
        setup(663,"cellwall_2", true);
        setup(664,"cellwall_3", true);

        setup(665,"cellwall_4", true);
        setup(666,"cellwall_5", true);
        setup(667,"cellwall_6", true);

        setup(668,"cell_door1", true);
        setup(669,"cell_door2", true);
        setup(670,"cell_door3", true);
        setup(671,"cell_door4", true);
        setup(672,"cell_door5", true);
        setup(673,"cell_door6", true);

        setup(674,"table2_1", true);
        setup(675,"table2_2", true);
        setup(676,"table2_3", true);
        setup(677,"table2_4", true);

        setup(678,"redcarpet_1", false);
        setup(679,"redcarpet_2", false);
        setup(680,"redcarpet_3", false);
        setup(681,"redcarpet_4", false);
        setup(682,"redcarpet_5", false);
        setup(683,"redcarpet_6", false);
        setup(684,"redcarpet_7", false);
        setup(685,"redcarpet_8", false);
        setup(686,"redcarpet_9", false);
        setup(687,"redcarpet_10", true);

        setup(688,"bagumbayan_wall_1", true);
        setup(689,"bagumbayan_wall_2", true);
        setup(690,"bagumbayan_wall_3", true);
        setup(691,"bagumbayan_wall_4", true);
        setup(692,"bagumbayan_wall_5", true);

        setup(693,"bagumbayan_wall_6", true);
        setup(694,"bagumbayan_wall_7", true);
        setup(695,"bagumbayan_wall_8", true);
        setup(696,"bagumbayan_wall_9", true);

        //setup(697,"", true);
        //setup(698,"", true);
        //setup(699,"", true);
        //setup(700,"", true);
        //setup(701,"", true);
        //setup(702,"", true);
        //setup(703,"", true);
        //setup(704,"", true);
        //setup(705,"", true);

        setup(706,"tree4_0", true);
        setup(707,"tree4_1", true);
        setup(708,"tree4_2", true);
        setup(709,"tree4_3", true);
        setup(710,"tree4_4", true);
        setup(711,"tree4_5", true);
        setup(712,"tree4_6", true);

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
        int playerScreenX = gp.player.screenX;
        int playerScreenY = gp.player.screenY;
        int playerWorldX  = gp.player.worldX;
        int playerWorldY  = gp.player.worldY;

        // Calculate the range of tiles visible on screen
        int startCol = Math.max(0, (playerWorldX - playerScreenX) / gp.tileSize);
        int endCol   = Math.min(gp.maxWorldCol - 1, startCol + gp.maxScreenCol + 1);
        int startRow = Math.max(0, (playerWorldY - playerScreenY) / gp.tileSize);
        int endRow   = Math.min(gp.maxWorldRow - 1, startRow + gp.maxScreenRow + 1);

        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {
                int tileNum = mapTileNum[col][row];
                if (tileNum < 0 || tileNum >= tile.length || tile[tileNum] == null) continue;

                int screenX = (col * gp.tileSize) - playerWorldX + playerScreenX;
                int screenY = (row * gp.tileSize) - playerWorldY + playerScreenY;
                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }
        }
    }
}