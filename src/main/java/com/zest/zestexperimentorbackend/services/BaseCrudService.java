package com.zest.zestexperimentorbackend.services;

import com.zest.zestexperimentorbackend.exceptions.BaseNotFoundExeption;
import com.zest.zestexperimentorbackend.persists.repositories.BaseRepository;

import java.util.List;

public class BaseCrudService<T>{
    public final BaseRepository<T> repository;

    public BaseCrudService(BaseRepository<T> repository) {
        this.repository = repository;
    }

    public void save(List<T> itemList){
        repository.saveAll(itemList);
    }

    public T saveOne(T item){
        return repository.save(item);
    }

    public T getById(String id){
        return repository.findById(id).orElseThrow(() -> new BaseNotFoundExeption(id,repository.getClass().toString()));
    }

    public void deleteById(String id){
        repository.deleteById(id);
    }



    public List<T> getAll(){
        return repository.findAll();
    }
}
