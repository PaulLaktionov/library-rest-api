package com.example.library.service;
import com.example.library.exception.NotFoundException;
import com.example.library.model.Reader;
import com.example.library.repository.ReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReaderService {
    private final ReaderRepository readerRepository;

    public List<Reader> getAll() {
        return readerRepository.findAll();
    }
    public Reader  getById(Long id){
        return readerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Reader.class, id));
    }
    public Reader  create(Reader reader){
        return readerRepository.save(reader);

    }
    public Reader update(Long id, Reader updated) {
        Reader reader = getById(id);
        reader.setName(updated.getName());
        reader.setEmail(updated.getEmail());
        return readerRepository.save(reader);

    }
    public void delete(Long id) {
        Reader reader = getById(id);
        readerRepository.delete(reader);
    }
}





