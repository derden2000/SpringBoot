package pro.antonshu.market.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.antonshu.market.entities.Group;
import pro.antonshu.market.repositories.GroupRepository;

import java.util.List;

@Service
public class GroupService {

    private GroupRepository groupRepository;

    @Autowired
    public void setCategoryRepository(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public boolean existByTitle(String title) {
        return groupRepository.existsByTitle(title);
    }

    public Group getGroupByTitle(String title) {
        return groupRepository.findOneByTitle(title);
    }

    public Group getGroupById(String groupId) {
        return groupRepository.findOneById((Long.parseLong(groupId)));
    }

}
