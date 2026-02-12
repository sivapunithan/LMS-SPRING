package com.punithan_library.Service.Impl;

import com.punithan_library.Exception.GenreException;
import com.punithan_library.Payload.DTO.GenreDTO;
import com.punithan_library.Entity.GenreEntity;
import com.punithan_library.Mapper.GenreMapper;
import com.punithan_library.Repository.GenreRepository;
import com.punithan_library.Service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private GenreMapper genreMapper;

    @Override
    public GenreDTO createGenre(GenreDTO genre) {
        GenreEntity genreEntity = GenreMapper.toEntity(genre);
        GenreEntity savedGenre = genreRepository.save(genreEntity);
        GenreDTO dto = GenreMapper.toDTO(savedGenre);
        return dto;
    }

    @Override
    public List<GenreDTO> getAllGenres() {
        return genreRepository.findAll().stream()
                .map(GenreMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GenreDTO getGenreById(Long genreId) throws GenreException {

        GenreEntity genre = genreRepository.findById(genreId)
                .orElseThrow(()-> new GenreException("Genre not Found"));
        return GenreMapper.toDTO(genre);
    }

    @Override
    public GenreDTO updateGenre(Long genreId, GenreDTO genreDto) throws GenreException {
        GenreEntity genre = genreRepository.findById(genreId).orElseThrow(
                ()-> new GenreException("Genre Not Found"));
        genreMapper.updateEntityFromDTO(genreDto, genre);
        GenreEntity savedGenre = genreRepository.save(genre);

        return GenreMapper.toDTO(savedGenre);
    }

    @Override
    public void deleteGenre(Long genreId) throws GenreException {
        GenreEntity genreEntity = genreRepository.findById(genreId).orElseThrow(
                ()-> new GenreException("Genre not Found"));
        genreEntity.setActive(false);
        genreRepository.save(genreEntity);
    }

    @Override
    public void hardDelete(Long genreId) throws GenreException {
        GenreEntity genre = genreRepository.findById(genreId).orElseThrow(
                ()-> new GenreException("Genre not found"));
        genreRepository.delete(genre);
    }

    @Override
    public List<GenreDTO> getAllActiveGenreWithSubGenres() {

        List<GenreEntity> genreEntities = genreRepository.findByParentGenreIsNullAndActiveTrueOrderByDisplayOrderAsc();
        return genreMapper.toDTOLIst(genreEntities);
    }

    @Override
    public List<GenreDTO> getTopLevelGenres() {

        List<GenreEntity> genreEntities = genreRepository.findByParentGenreIsNullAndActiveTrueOrderByDisplayOrderAsc();
        return genreMapper.toDTOLIst(genreEntities);
    }

    @Override
    public long getTotalActiveGenres() {
        return genreRepository.countByActiveTrue();
    }

    @Override
    public long getBookCountByGenre(Long genreId) {
        return 0;
    }
}
