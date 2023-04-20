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

    private MovieInfoDto movieInfoMovieInfoDtoMapper(MovieInfo movieInfo) {
        return new MovieInfoDto(movieInfo.getId(),
                movieInfo.getName(),
                movieInfo.getYear(),
                movieInfo.getActors(),
                movieInfo.getReleaseDate());

    }

    public Flux<MovieInfoDto> getMovieInfo() {
        return repository.findAll()
                .map(this::movieInfoMovieInfoDtoMapper);

    }

    public Mono<MovieInfoDto> getMovieInfoById(String id) {
        return repository.findById(id)
                .map(this::movieInfoMovieInfoDtoMapper);
    }

    public Mono<MovieInfoDto> addMovieInfo(MovieInfoDto movieInfoDto) {

        var movieInfo = new MovieInfo(movieInfoDto.id(),
                movieInfoDto.name(),
                movieInfoDto.year(),
                movieInfoDto.actors(),
                movieInfoDto.releaseDate());

        return repository.save(movieInfo)
                .map(this::movieInfoMovieInfoDtoMapper);
    }

    public Mono<MovieInfoDto> updateMovieInfo(MovieInfoDto movieInfoDto, String id) {

        return repository.findById(id)
                .flatMap(movieInfo -> {
                    movieInfo.setName(movieInfoDto.name());
                    movieInfo.setYear(movieInfoDto.year());
                    movieInfo.setActors(movieInfoDto.actors());
                    movieInfo.setReleaseDate(movieInfoDto.releaseDate());

                    return repository.save(movieInfo)
                            .map(this::movieInfoMovieInfoDtoMapper);

                });
    }

    public Mono<Void> deleteMovieInfo(String id) {
        return repository.deleteById(id);
    }

    public Flux<MovieInfoDto> getMovieInfosByYear(int year) {
        return repository.findMovieInfoByYear(year)
                .map(this::movieInfoMovieInfoDtoMapper);
    }
}
