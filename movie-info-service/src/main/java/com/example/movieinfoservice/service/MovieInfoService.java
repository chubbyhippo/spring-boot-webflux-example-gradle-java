package com.example.movieinfoservice.service;

import com.example.movieinfoservice.document.MovieInfo;
import com.example.movieinfoservice.dto.MovieInfoDto;
import com.example.movieinfoservice.repository.MovieInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MovieInfoService {

    private final MovieInfoRepository repository;

    public Flux<MovieInfoDto> getMovieInfo() {
        return repository.findAll()
                .map(movieInfo -> new MovieInfoDto(movieInfo.getId(),
                        movieInfo.getName(),
                        movieInfo.getYear(),
                        movieInfo.getCast(),
                        movieInfo.getReleaseDate()));

    }

    public Mono<MovieInfoDto> getMovieInfoById(String id) {
        return repository.findById(id)
                .map(movieInfo -> new MovieInfoDto(movieInfo.getId(),
                        movieInfo.getName(),
                        movieInfo.getYear(),
                        movieInfo.getCast(),
                        movieInfo.getReleaseDate()));
    }

    public Mono<MovieInfoDto> addMovieInfo(MovieInfoDto movieInfoDto) {

        var movieInfo = new MovieInfo(movieInfoDto.id(),
                movieInfoDto.name(),
                movieInfoDto.year(),
                movieInfoDto.cast(),
                movieInfoDto.releaseDate());

        return repository.save(movieInfo)
                .map(savedMovieInfo -> new MovieInfoDto(savedMovieInfo.getId(),
                        savedMovieInfo.getName(),
                        savedMovieInfo.getYear(),
                        savedMovieInfo.getCast(),
                        savedMovieInfo.getReleaseDate()));
    }

}
