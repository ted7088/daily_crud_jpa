<template>
  <div class="todo-list">
    <div class="list-header">
      <h2>할일 목록</h2>
      
      <!-- 선택 삭제 컨트롤 -->
      <div v-if="todos.length > 0" class="selection-controls">
        <label class="select-all">
          <input
            type="checkbox"
            :checked="isAllSelected"
            @change="toggleSelectAll"
          />
          <span>전체 선택</span>
        </label>
        <button
          v-if="selectedIds.length > 0"
          class="btn-delete-selected"
          @click="handleDeleteSelected"
        >
          선택 삭제 ({{ selectedIds.length }})
        </button>
      </div>

      <div class="filter-buttons">
        <button
          :class="['filter-btn', { active: filter === 'all' }]"
          @click="filter = 'all'"
        >
          전체 ({{ todos.length }})
        </button>
        <button
          :class="['filter-btn', { active: filter === 'active' }]"
          @click="filter = 'active'"
        >
          진행중 ({{ activeTodos.length }})
        </button>
        <button
          :class="['filter-btn', { active: filter === 'completed' }]"
          @click="filter = 'completed'"
        >
          완료 ({{ completedTodos.length }})
        </button>
      </div>
    </div>

    <div v-if="filteredTodos.length === 0" class="empty-state">
      <p>{{ emptyMessage }}</p>
    </div>

    <div v-else class="todo-items">
      <TodoItem
        v-for="todo in filteredTodos"
        :key="todo.id"
        :todo="todo"
        :is-selected="selectedIds.includes(todo.id)"
        @toggle="$emit('toggle', $event)"
        @select="toggleSelection"
        @edit="$emit('edit', $event)"
        @delete="$emit('delete', $event)"
      />
    </div>
  </div>
</template>

<script>
import TodoItem from './TodoItem.vue';

export default {
  name: 'TodoList',
  components: {
    TodoItem
  },
  props: {
    todos: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      filter: 'all',
      selectedIds: []
    };
  },
  computed: {
    activeTodos() {
      return this.todos.filter(todo => !todo.completed);
    },
    completedTodos() {
      return this.todos.filter(todo => todo.completed);
    },
    filteredTodos() {
      switch (this.filter) {
        case 'active':
          return this.activeTodos;
        case 'completed':
          return this.completedTodos;
        default:
          return this.todos;
      }
    },
    emptyMessage() {
      switch (this.filter) {
        case 'active':
          return '진행중인 할일이 없습니다 👍';
        case 'completed':
          return '완료된 할일이 없습니다';
        default:
          return '할일을 추가해보세요! 📝';
      }
    },
    isAllSelected() {
      return this.filteredTodos.length > 0 && 
             this.filteredTodos.every(todo => this.selectedIds.includes(todo.id));
    }
  },
  methods: {
    toggleSelection(todoId) {
      const index = this.selectedIds.indexOf(todoId);
      if (index > -1) {
        this.selectedIds.splice(index, 1);
      } else {
        this.selectedIds.push(todoId);
      }
    },
    toggleSelectAll() {
      if (this.isAllSelected) {
        // 현재 필터된 항목들을 선택 해제
        this.filteredTodos.forEach(todo => {
          const index = this.selectedIds.indexOf(todo.id);
          if (index > -1) {
            this.selectedIds.splice(index, 1);
          }
        });
      } else {
        // 현재 필터된 항목들을 모두 선택
        this.filteredTodos.forEach(todo => {
          if (!this.selectedIds.includes(todo.id)) {
            this.selectedIds.push(todo.id);
          }
        });
      }
    },
    handleDeleteSelected() {
      if (this.selectedIds.length === 0) return;
      this.$emit('delete-multiple', [...this.selectedIds]);
      this.selectedIds = []; // 삭제 후 선택 초기화
    }
  }
};
</script>

<style scoped>
.todo-list {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.list-header {
  margin-bottom: 1.5rem;
}

.list-header h2 {
  margin: 0 0 1rem 0;
  color: #2c3e50;
  font-size: 1.5rem;
}

.selection-controls {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1rem;
  padding: 0.75rem;
  background: #f8f9fa;
  border-radius: 8px;
}

.select-all {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
  font-size: 0.95rem;
  color: #555;
}

.select-all input[type="checkbox"] {
  width: 18px;
  height: 18px;
  cursor: pointer;
  accent-color: #ff5722;
}

.btn-delete-selected {
  padding: 0.5rem 1rem;
  background: linear-gradient(135deg, #ff5722 0%, #f44336 100%);
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-delete-selected:hover {
  background: linear-gradient(135deg, #f44336 0%, #d32f2f 100%);
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(255, 87, 34, 0.3);
}

.filter-buttons {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.filter-btn {
  padding: 0.5rem 1rem;
  border: 2px solid #e0e0e0;
  border-radius: 20px;
  background: white;
  color: #666;
  font-size: 0.9rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.filter-btn:hover {
  border-color: #42b983;
  color: #42b983;
}

.filter-btn.active {
  background: linear-gradient(135deg, #42b983 0%, #35a372 100%);
  border-color: #42b983;
  color: white;
}

.empty-state {
  text-align: center;
  padding: 3rem 1rem;
  color: #999;
}

.empty-state p {
  font-size: 1.1rem;
  margin: 0;
}

.todo-items {
  margin-top: 1rem;
}
</style>
