/*
 * 
 * You can use the following import statements
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.ArrayList;
 * 
 */

// Write your code here
package com.example.song.service;

import com.example.song.repository.SongRepository;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import com.example.song.model.Song;
import com.example.song.model.SongRowMapper;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
@Service
public class SongH2Service implements SongRepository{
    @Autowired
    public JdbcTemplate db;
    @Override
    public ArrayList<Song> getSongs(){
        List<Song> songList = db.query("select * from playlist", new SongRowMapper());
        ArrayList<Song> songs = new ArrayList<>(songList);
        return songs;
    }

    @Override
    public Song getSongById(int songId){
        try{
        Song song = db.queryForObject("select * from playlist where songId=?", new SongRowMapper(), songId);
        return song;
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Song addSong(Song song){
        db.update("insert into playlist(songName, lyricist, singer, musicDirector) values(?,?,?,?)", song.getSongName(), song.getLyricist(), song.getSinger(), song.getMusicDirector());
        Song savedSong = db.queryForObject("select * from playlist where songName=? and lyricist=? and singer=? and musicDirector=?", new SongRowMapper(), song.getSongName(), song.getLyricist(), song.getSinger(), song.getMusicDirector());
        return savedSong;
    }

    @Override
    public Song updateSong(int songId, Song song){
        try{
        

        
        if (song.getSongId()!=0){
            db.update("update playlist set songId=? where songId=?", song.getSongId(), songId);
        }
        if (song.getSongName()!=null){
            db.update("update playlist set songName=? where songId=?", song.getSongName(), songId);
        }
        if (song.getLyricist()!=null){
            db.update("update playlist set lyricist=? where songId=?", song.getLyricist(), songId);
        }
        if (song.getSinger()!=null){
            db.update("update playlist set singer=? where songId=?", song.getSinger(), songId);
        }
        if (song.getMusicDirector()!=null){
            db.update("update playlist set musicDirector=? where songId=?", song.getMusicDirector(), songId);
        }
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        
        return getSongById(songId);
    }

    @Override
    public void deleteSong(int songId){
        db.update("delete from playlist where songId=?",songId);
    }

}