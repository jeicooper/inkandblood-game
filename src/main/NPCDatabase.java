package main;

import java.io.*;
import java.util.*;

public class NPCDatabase {

    GamePanel   gp;
    UserManager userManager;

    // NPC LIST
    // { id, name, nickname, role, chapter, quest, bio }
    private static final String[][] NPC_MASTER = {

            { "teodora",
                    "Teodora Alonzo Realonda",            "Nanay",
                    "Mother of Jose Rizal",      "Chapter 1", "Quest 1: Familya Rizal",
                    "A woman of remarkable intelligence and literary talent, Teodora Alonzo was Rizal's mother as well as his first teacher. She was born in Meisik, Tondo, Manila and was known for being a disciplinarian as well as a dedicated, courteous and hard-working mother. She was 20 years old when she married Francisco Mercado of Biñan, Laguna in 1848. They settled in Calamba, Laguna and to them were born eleven children. She had a profound influence on Rizal's development and was his inspiration in taking up medicine. \n As the mother of a perceived enemy of the Spanish authorities, Teodora was often persecuted. In 1872, she was imprisoned for two and a half years on trumped-up charges of poisoning her brother's wife."},

            { "francisco",
                    "Francisco Engracio Rizal Mercado",           "Tatay",
                    "Father of Jose Rizal",      "Chapter 1", "Quest 1: Familya Rizal",
                    "Francisco was the father of Jose Rizal. He and his wife Teodora Alonzo had 11 children together. He attended a Latin school in Biñan, which his sons would later attend. He also attended the Colegio de San Jose in Manila, where he studied Latin and philosophy. A hardworking tenant farmer and one of the first in Laguna to establish a sugar mill. His dignity in the face of colonial hardship was not lost on young Jose." },

            { "concha",
                    "Conception Rizal",              "Concha",
                    "Youngest Sibling",          "Chapter 1", "Quest 1: Familya Rizal",
                    "Conception Rizal was the youngest sister of José Protasio Rizal Mercado y Alonso Realonda and the first among his siblings to die in childhood. Her early passing deeply affected Rizal, marking his first experience with grief and loss. This event shaped his sensitivity and emotional depth, which later appeared in his writings. Though she lived only a short life, Concepción’s memory remained significant to Rizal, symbolizing innocence and the harsh realities of life under difficult conditions." },

            { "saturnina",
                    "Saturnina Rizal",           "Neneng",
                    "Eldest Sister",             "Chapter 1", "Quest 1: Familya Rizal",
                    "Saturnina Rizal, the eldest sibling, played a maternal role in Rizal’s upbringing. She supported his education financially and emotionally, especially during his early schooling. Her guidance helped nurture his discipline and ambition. Saturnina maintained a close bond with Rizal, often corresponding with him during his travels. Her steady presence in his life contributed to his strong family values and sense of responsibility." },

            { "paciano",
                    "Paciano Rizal",             "Ute",
                    "Elder Brother & Protector", "Chapter 1", "Quest 1: Familya Rizal",
                    "Paciano Rizal was Rizal’s only brother and one of his greatest influences. He supported Rizal’s education and shared his nationalist ideals. Paciano had connections with reformists and was deeply affected by the execution of Gomburza, which also influenced Rizal. Acting as a mentor, he guided Rizal in understanding the injustices of Spanish rule, helping shape his political awareness and reformist views." },

            { "narcisa",
                    "Narcisa Rizal",             "Sisa",
                    "Sibling",                   "Chapter 1", "Quest 1: Familya Rizal",
                    "Narcisa Rizal was a supportive sister who played an important role after Rizal’s execution by helping locate his grave. Throughout his life, she maintained communication with him and showed strong family loyalty. Her actions reflected courage and devotion, ensuring that Rizal’s memory and dignity were preserved even after his death." },

            { "olimpia",
                    "Olimpia Rizal",             "Ypia",
                    "Sibling",                   "Chapter 1", "Quest 1: Familya Rizal",
                    "Olimpia Rizal was one of Rizal’s sisters who lived a relatively quiet life but remained part of his close-knit family. Though less publicly involved, she contributed to the supportive environment that shaped Rizal’s character. Her presence reflects the strong familial ties that Rizal valued deeply." },

            { "lucia",
                    "Lucia Rizal",               "Lucing",
                    "Sibling",                   "Chapter 1", "Quest 1: Familya Rizal",
                    "Lucia Rizal was known for her strength during family hardships, particularly when facing conflicts with Spanish authorities. Her experiences with injustice reinforced Rizal’s awareness of colonial oppression. Lucia’s resilience mirrored the struggles of Filipino families under Spanish rule." },

            { "maria",
                    "Maria Rizal",               "Biang",
                    "Sibling",                   "Chapter 1", "Quest 1: Familya Rizal",
                    "Maria Rizal was a caring and supportive sister who maintained close ties with Rizal. She was known for her kindness and dedication to family. Her steady support contributed to Rizal’s emotional well-being, especially during his years abroad." },

            { "josefa",
                    "Josefa Rizal",              "Panggoy",
                    "Sibling",                   "Chapter 1", "Quest 1: Familya Rizal",
                    "Josefa Rizal, despite suffering from health issues, remained devoted to her brother. Her condition did not hinder her loyalty and affection. She symbolizes the quiet strength within the Rizal family, supporting him despite personal challenges." },

            { "trinidad",
                    "Trinidad Rizal",            "Trining",
                    "Sibling",                   "Chapter 1", "Quest 1: Familya Rizal",
                    "Trinidad Rizal is remembered for preserving Rizal’s final poem, Mi Último Adiós. She received it hidden inside a lamp, ensuring its survival. Her role was crucial in safeguarding one of Rizal’s most important literary works, reflecting her trustworthiness and courage." },

            { "soledad",
                    "Soledad Rizal",             "Choleng",
                    "Sibling",                   "Chapter 1", "Quest 1: Familya Rizal",
                    "Soledad Rizal, the youngest surviving sibling, admired her brother greatly. Though she was young during Rizal’s lifetime, she carried his legacy forward. Her life reflects the lasting influence Rizal had on his family." },

            { "jose",
                    "Jose Alberto",              "Uncle Jose",
                    "Artistic Uncle",            "Chapter 1", "Quest 2: Pangangaral ng mga Tiyo",
                    "A gifted painter who saw the spark of creativity in young Jose. He tasked his nephew with gathering art supplies as a lesson " +
                            "in patience and dedication. He encouraged Rizal to explore the arts, specifically painting, sketching, and sculpture. " +
                            "encouragement helped Rizal develop his artistic talents, which he showcased in various works throughout his life. Rizal's artistic pursuits were not just hobbies; they were integral to his identity and his ability to express his thoughts on nationalism." },

            { "manuel",
                    "Manuel Hidalgo",            "Uncle Manuel",
                    "Athletic Uncle",            "Chapter 1", "Quest 2: Pangangaral ng mga Tiyo",
                    "Uncle Manuel believed a sharp mind must live in a strong body. He designed a course through Calamba's fields, teaching Jose " +
                            "that perseverance must be practiced, not just preached. Uncle Manuel introduced Rizal to physical activities such as " +
                            "swimming, fencing, and wrestling. Engaging in sports helped Rizal develop discipline, resilience, and a competitive " +
                            "spirit, traits that would serve him well in his later endeavors. The emphasis on physical fitness and sports reflects the holistic approach to education that Rizal experienced in his youth." },

            { "gregorio",
                    "Gregorio Alonzo",            "Uncle Gregorio",
                    "Scholar Uncle",             "Chapter 1", "Quest 2: Pangangaral ng mga Tiyo",
                    "A man of letters who saw in Jose a mind hungry for knowledge. He provided Jose with his first quill and notebook, igniting a " +
                            "lifelong love of writing. Uncle Gregorio was known for his passion for literature and books, which likely instilled a " +
                            "love for reading in Rizal. His influence may have contributed to Rizal's later writings, including his novels that " +
                            "addressed social issues in the Philippines. The importance of literature in Rizal's life can be seen in his extensive reading habits and his own literary contributions." },

            { "ferrando",
                    "Fr. Pablo Ferrando",        "Father Ferrando",
                    "Ateneo Prefect",            "Chapter 2", "Quest 3: Ang Bagong Simula",
                    "The Prefect of Discipline at Ateneo Municipal de Manila. Initially dismissive of a boy from Calamba, Ferrando was won over after Rizal aced the academic quiz and personally awarded him the Sobresaliente medal." },

            { "burgos",
                    "Pedro Burgos",              "Senor Burgos",
                    "Reformist Priest",          "Chapter 2", "Quest 3: Ang Bagong Simula",
                    "A Filipino secular priest who advocated for the rights of native clergy. His execution in 1872 became a symbol of colonial injustice that deeply marked Rizal. Pedro Burgos was the father of Jose Burgos, one of the Gomburza priests. His connection indirectly influenced Rizal, as the execution of Gomburza became a major inspiration for Rizal’s reformist ideals." },

            { "professor",
                    "Prof. Francisco de Paula Sanchez",           "Professor",
                    "Ateneo Professor",          "Chapter 2", "Quest 3: Ang Bagong Simula",
                    "Prof. Francisco de Paula Sanchez was Rizal’s favorite teacher at Ateneo. He recognized Rizal’s talent and encouraged his intellectual growth. His mentorship helped develop Rizal’s skills in writing and critical thinking. He who guided Jose through his first academic challenge at Ateneo. He believed that knowledge was the only weapon worth carrying." },

            { "interno",
                    "Classmate",                 "Interno",
                    "Ateneo Student",            "Chapter 2", "Quest 3: Ang Bagong Simula",
                    "Internos were students who lived inside Ateneo. Rizal initially belonged to this group, which exposed him to stricter discipline and structured learning. This environment shaped his academic excellence." },
            { "externo",
                "Classmate",                    "Externo",
                "Ateneo Student",               "Chapter 2",
                    "Quest 3: Ang Bagong Simula",
                    "Externos were students who lived outside Ateneo. Rizal later became one, giving him more independence while continuing his studies. This shift reflected his growing maturity."
            },

            { "mariano",
                    "Mariano Katigbak",          "Mariano",
                    "Fellow Student",            "Chapter 2", "Quest 4: Ang Kampeon ng Roma",
                    "A classmate and confidant at Ateneo who encouraged Jose to enter the discipline competitions. His rivalry with Jose was always friendly — each pushing the other to greater heights. Mariano Katigbak was Rizal’s close friend and brother of his first love. Their friendship represents Rizal’s early social life and emotional development during his youth." },

            { "rector",
                    "Fr. Pablo Ramon",                "The Rector",
                    "Ateneo Rector",             "Chapter 2", "Quest 4: Ang Kampeon ng Roma",
                    "The head of Ateneo Municipal de Manila who oversaw the discipline competitions. He recognized in Rizal an extraordinary scholar and presided over the awarding of his medals." },

            { "perfect",
                    "Prefect of Conduct",        "Prefect",
                    "Discipline Judge",          "Chapter 2", "Quest 4: Ang Kampeon ng Roma",
                    "The judge for the Conduct discipline. He evaluated not just behavior but character — testing whether Jose could hold composure under pressure." },

            { "casimiro",
                    "Fr. Casimiro",              "Casimiro",
                    "Painting Judge",            "Chapter 2", "Quest 4: Ang Kampeon ng Roma",
                    "The judge for the Painting discipline. He saw in Rizal's brushwork the same precision and passion that defined all of the young man's pursuits." },

            { "millano",
                    "Fr. Millano",               "Millano",
                    "French Language Judge",     "Chapter 2", "Quest 4: Ang Kampeon ng Roma",
                    "The French Language judge. He tested Jose's grasp of the language that would later help him navigate the salons of Europe." },

            { "desanctis",
                    "Fr. De Sanctis",            "De Sanctis",
                    "Rhetoric Judge",            "Chapter 2", "Quest 4: Ang Kampeon ng Roma",
                    "The Rhetoric judge. He challenged Jose to argue with both fire and structure — a skill Rizal would refine into the most powerful prose the Philippines had ever seen." },

            { "pedro",
                    "Pedro Paterno",             "Pedro",
                    "Patron of the Arts",        "Chapter 3", "Quest 5: Noli Me Tangere",
                    "A wealthy Filipino intellectual and writer in Madrid who provided moral and financial support to Rizal as he worked on his novel. He understood the weight of what Rizal was trying to do. He was a Filipino intellectual who later negotiated with Spanish authorities during Rizal’s time. Though controversial, he was part of the political environment surrounding Rizal." },

            { "consuelo",
                    "Consuelo Ortiga",           "Consuelo",
                    "Friend in Madrid",          "Chapter 3", "Quest 5: Noli Me Tangere",
                    "A charming and intelligent woman in Madrid's Filipino expatriate circle. Her friendship helped sustain Rizal during the lonely, grueling months of writing Noli Me Tangere. Their connection reflects Rizal’s romantic side, though it did not develop into a serious relationship." },

            { "maximo",
                    "Maximo Viola",              "Maximo",
                    "Financier of the Noli",     "Chapter 3", "Quest 5: Noli Me Tangere",
                    "Maximo Viola was the man who made the Noli Me Tangere possible. When Rizal had no money left to print his novel, Viola provided the funds. He received the first signed copy in return. He was one of Rizal’s closest friends and financial supporter. He helped fund the publication of Noli Me Tangere. His loyalty and belief in Rizal’s work were crucial to its success." },

            { "paciano_q6",
                    "Paciano Rizal",             "Kuya Paciano",
                    "Brother & Confidant",       "Chapter 3", "Quest 6: El Filibusterismo",
                    "Returning in a new capacity, Paciano was Jose's most trusted correspondent during the exile years. He relayed news from Calamba and channeled resources to his brother across the ocean." },
            { "guardiacivil",
                    "Guardia Civil",                     "Guardia",
                    "Colonial Officer",          "Chapter 4", "Quest 7: Consummatum Est",
                    "A soldier of the Spanish colonial authority stationed at Cuartel de España. " +
                            "He presented the evidence gathered against Rizal — letters found among rebels, " +
                            "photographs displayed as war banners — and insisted that Rizal's pen was the spark " +
                            "that lit the revolution. He saw no doctor; he saw only a threat." },

            { "judge",
                    "The Judge of Cuartel de España",    "The Judge",
                    "Military Judge",            "Chapter 4", "Quest 7: Consummatum Est",
                    "The presiding officer of the military council that tried José Rizal in December 1896. " +
                            "He found Rizal guilty of illegal association, rebellion, and sedition, and signed " +
                            "the death warrant ordering him to be shot at Bagumbayan on the morning of " +
                            "December 30, 1896. His verdict ended the trial of the most famous Filipino " +
                            "who ever stood before a colonial court." },

            { "josephine",
                    "Marie Bernadette Josephine Bracken",                 "Dulce Extranjera",
                    "Common-law Wife",           "Chapter 4", "Quest 7: Consummatum Est",
                    "Born in Hong Kong to Irish parents, Josephine arrived in the remote town of Dapitan accompanying her blind adoptive father. She soon fell in love with Rizal, becoming his constant companion and domestic partner during his final years of exile. Despite the lack of a formal church wedding and the initial suspicion of Rizal's family, she remained fiercely loyal. She lived with him in Fort Santiago during his final days and, following his execution, she bravely joined the revolutionary forces in Cavite, even participating in field operations against the Spanish" },
    };

    public Set<String> unlockedNPCs = new HashSet<>();
    public int dexSelectedIndex = 0;
    public int dexScrollOffset = 0;

    public NPCDatabase(GamePanel gp, UserManager userManager) {
        this.gp = gp;
        this.userManager = userManager;
    }

    public void unlock(String dexId) {
        if (dexId == null || dexId.isEmpty()) return;
        unlockedNPCs.add(dexId);
    }

    public boolean isUnlocked(String id) {
        return unlockedNPCs.contains(id);
    }
    public int getTotalCount() {
        return NPC_MASTER.length;
    }
    public int getUnlockedCount() {
        return unlockedNPCs.size();
    }

    public String getId(int i)       { return NPC_MASTER[i][0]; }
    public String getName(int i)     { return NPC_MASTER[i][1]; }
    public String getNickname(int i) { return NPC_MASTER[i][2]; }
    public String getRole(int i)     { return NPC_MASTER[i][3]; }
    public String getChapter(int i)  { return NPC_MASTER[i][4]; }
    public String getQuest(int i)    { return NPC_MASTER[i][5]; }
    public String getBio(int i)      { return NPC_MASTER[i][6]; }


    public void save() {
        if (!userManager.isLoggedIn()) return;
        File f = getFile();
        try {
            f.getParentFile().mkdirs();
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            for (String id : unlockedNPCs) {
                bw.write(id);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        reset();
        if (!userManager.isLoggedIn()) return;
        File f = getFile();
        if (!f.exists()) return;
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) unlockedNPCs.add(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteForUser(String username) {
        File f = new File("saves" + File.separator + username.trim() + "_npcdb.dat");
        if (f.exists()) f.delete();
    }

    public void reset() {
        unlockedNPCs.clear();
        dexSelectedIndex = 0;
        dexScrollOffset  = 0;
    }

    private File getFile() {
        String base = userManager.getSaveFile().getPath();
        return new File(base.replace(".dat", "_npcdb.dat"));
    }
}