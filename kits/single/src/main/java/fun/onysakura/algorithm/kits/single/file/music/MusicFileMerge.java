package fun.onysakura.algorithm.kits.single.file.music;

import fun.onysakura.algorithm.utils.core.basic.str.StringUtils;
import fun.onysakura.algorithm.utils.db.sqlite.BaseRepository;
import fun.onysakura.algorithm.utils.db.sqlite.SQLite;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class MusicFileMerge {

    private static final String FINAL_PATH = "R:/Files/Music/new";
    private static final String DEL_PATH = "R:/Files/Music/del";

    public static void main(String[] args) throws Exception {
        SQLite.open("R:/Files/Music/all.db");
        findDuplicateFiles("R:/Files/Music/old1", "R:/Files/Music/old2");
    }

    public static void findDuplicateFiles(String... dirPaths) throws Exception {
        BaseRepository<Music> musicRepository = new BaseRepository<>(Music.class);
        musicRepository.createTable();
        for (String dirPath : dirPaths) {
            File dir = new File(dirPath);
            if (!dir.exists() || !dir.isDirectory()) {
                log.warn("path is not directory");
            }
            File[] files = dir.listFiles();
            if (files == null) {
                log.warn("no file in " + dirPath);
                continue;
            }
            for (File file : files) {
                String fileName = file.getName();
                String length = String.valueOf(file.length());
                int lastIndexOf = fileName.lastIndexOf(".");
                String musicName = fileName.substring(0, lastIndexOf);
                String fileSuffix = fileName.substring(lastIndexOf);
                List<Music> musicList = musicRepository.selectAll();
                int opt = 0; // new
                Music temp = null;
                musicList:
                for (Music music : musicList) {
                    if (StringUtils.levenshtein(music.getName(), musicName) > 80 || music.getSize().equals(length)) {
                        if (music.getName().equals(musicName) && music.getSize().equals(length)) {
                            opt = -1;
                            break;
                        }
                        log.info("check -> \n{}\n{}",
                                String.format("old %20s, %s", music.getSize(), music.getFullName()),
                                String.format("new %20s, %s, ", length, fileName));
                        log.info("input add, delete or replace: ");
                        Scanner scanner = new Scanner(System.in);
                        String input = scanner.nextLine();
                        switch (input) {
                            case "a":
                            case "add":
                            case "A":
                                opt = 0;
                                continue musicList;
                            case "d":
                            case "D":
                                opt = -1; // delete
                                break musicList;
                            case "r":
                            case "R":
                                temp = music;
                                opt = 1; // replace old
                                break musicList;
                        }
                    }
                }
                switch (opt) {
                    case 0 -> { // add
                        Files.move(file.toPath(), new File(FINAL_PATH + "/" + fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
                        Music music = new Music();
                        music.setName(musicName);
                        music.setFullName(fileName);
                        music.setSize(length);
                        music.setType(fileSuffix);
                        musicRepository.insert(music);
                    }
                    case 1 -> { // replace
                        Files.move(new File(FINAL_PATH + "/" + temp.getFullName()).toPath(), new File(DEL_PATH + "/r_" + temp.getFullName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                        temp.setName(musicName);
                        temp.setFullName(fileName);
                        temp.setSize(length);
                        temp.setType(fileSuffix);
                        musicRepository.update(temp);
                        Files.move(file.toPath(), new File(FINAL_PATH + "/" + fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                    case -1 -> // delete
                            Files.move(file.toPath(), new File(DEL_PATH + "/d_" + fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }
}
