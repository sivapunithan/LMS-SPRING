package com.punithan_library.Mapper;

import com.punithan_library.Payload.DTO.GenreDTO;
import com.punithan_library.Entity.GenreEntity;
import com.punithan_library.Repository.GenreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class GenreMapper {
    private static GenreRepository genreRepository;

    public static GenreDTO toDTO(GenreEntity savedGenre){
        if (savedGenre == null){
            return null;
        }
        GenreDTO dto = new GenreDTO().builder()
                .id(savedGenre.getId())
                .genreCode(savedGenre.getGenreCode())
                .genreName(savedGenre.getGenreName())
                .description(savedGenre.getDescription())
                .displayOrder(savedGenre.getDisplayOrder())
                .active(savedGenre.getActive())
                .createdAt(savedGenre.getCreationAt())
                .updatedAt(savedGenre.getUpdateAt())
                .build();
        if(savedGenre.getParentGenre() != null){
            dto.setParentGenreId(savedGenre.getParentGenre().getId());
            dto.setParentGenreName(savedGenre.getParentGenre().getGenreName());
        }
        if (savedGenre.getSubGenres() != null && !savedGenre.getSubGenres().isEmpty()) {
            dto.setSubGenre(savedGenre.getSubGenres().stream()
                    .filter(subGenre -> subGenre.getActive())
                    .map(subGenre -> toDTO(subGenre)).collect(Collectors.toList()));
        }
        return dto;
    }

    public static GenreEntity toEntity(GenreDTO genre){
        if (genre == null){
            return null;
        }
        GenreEntity genreEntity = GenreEntity.builder()
                .genreCode(genre.getGenreCode())
                .genreName(genre.getGenreName())
                .description(genre.getDescription())
                .displayOrder(genre.getDisplayOrder())
                .active(genre.getActive())
                .build();
        if(genre.getParentGenreId() != null){
            genreRepository.findById(genre.getParentGenreId())
                    .ifPresent(genreEntity1 -> genreEntity.setParentGenre(genreEntity1.getParentGenre()));
            //genreEntity.setParentGenre(parentGenre);
        }
        return  genreEntity;
    }

    public void updateEntityFromDTO(GenreDTO genreDTO, GenreEntity genreEntity){
        if (genreDTO == null || genreEntity == null){
            return;
        }
        genreEntity.setGenreCode(genreDTO.getGenreCode());
        genreEntity.setGenreName(genreDTO.getGenreName());
        genreEntity.setDescription(genreDTO.getDescription());
        genreEntity.setDisplayOrder(genreDTO.getDisplayOrder() != null ? genreDTO.getDisplayOrder() : 0 );
        genreEntity.setActive(genreDTO.getActive() != null ? genreDTO.getActive() : false );
        if (genreDTO.getParentGenreId() != null){
            genreRepository.findById(genreDTO.getParentGenreId())
                    .ifPresent(genreEntity1 -> genreEntity1.setParentGenre(genreEntity));
        }
    }

    public List<GenreDTO> toDTOLIst(List<GenreEntity> genreEntities){
        return genreEntities.stream().map(entity -> toDTO(entity)).collect(Collectors.toList());
    }

}
