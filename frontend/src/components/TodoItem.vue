<template>
  <div class="todo-item" :class="{ completed: todo.completed, selected: isSelected }">
    <!-- 선택 체크박스 (삭제용) -->
    <div class="select-checkbox">
      <input
        type="checkbox"
        :checked="isSelected"
        @change="$emit('select', todo.id)"
      />
    </div>

    <!-- 완료 상태 체크박스 -->
    <div class="todo-checkbox">
      <input
        type="checkbox"
        :checked="todo.completed"
        @change="$emit('toggle', todo.id)"
      />
    </div>

    <div class="todo-content">
      <h3 class="todo-title">{{ todo.title }}</h3>
      <p v-if="todo.description" class="todo-description">
        {{ todo.description }}
      </p>
      <div class="todo-meta">
        <span class="todo-date">
          {{ formatDate(todo.createdAt) }}
        </span>
      </div>
    </div>

    <div class="todo-actions">
      <button
        class="btn-icon btn-edit"
        @click="$emit('edit', todo)"
        title="수정"
      >
        ✏️
      </button>
      <button
        class="btn-icon btn-delete"
        @click="$emit('delete', todo.id)"
        title="삭제"
      >
        🗑️
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TodoItem',
  props: {
    todo: {
      type: Object,
      required: true
    },
    isSelected: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    formatDate(dateString) {
      const date = new Date(dateString);
      const now = new Date();
      const diff = now - date;
      const days = Math.floor(diff / (1000 * 60 * 60 * 24));

      if (days === 0) {
        return '오늘';
      } else if (days === 1) {
        return '어제';
      } else if (days < 7) {
        return `${days}일 전`;
      } else {
        return date.toLocaleDateString('ko-KR');
      }
    }
  }
};
</script>

<style scoped>
.todo-item {
  display: flex;
  align-items: flex-start;
  gap: 1rem;
  padding: 1.25rem;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.08);
  transition: all 0.3s;
  margin-bottom: 1rem;
}

.todo-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.todo-item.completed {
  opacity: 0.7;
  background: #f9f9f9;
}

.todo-item.selected {
  background: #e8f5e9;
  border: 2px solid #42b983;
}

.select-checkbox {
  flex-shrink: 0;
  padding-top: 0.25rem;
}

.select-checkbox input[type="checkbox"] {
  width: 20px;
  height: 20px;
  cursor: pointer;
  accent-color: #ff5722;
}

.todo-checkbox {
  flex-shrink: 0;
  padding-top: 0.25rem;
}

.todo-checkbox input[type="checkbox"] {
  width: 20px;
  height: 20px;
  cursor: pointer;
  accent-color: #42b983;
}

.todo-content {
  flex: 1;
  min-width: 0;
}

.todo-title {
  margin: 0 0 0.5rem 0;
  color: #2c3e50;
  font-size: 1.1rem;
  font-weight: 600;
  word-wrap: break-word;
}

.todo-item.completed .todo-title {
  text-decoration: line-through;
  color: #999;
}

.todo-description {
  margin: 0 0 0.75rem 0;
  color: #666;
  font-size: 0.95rem;
  line-height: 1.5;
  word-wrap: break-word;
}

.todo-item.completed .todo-description {
  color: #aaa;
}

.todo-meta {
  display: flex;
  gap: 1rem;
  font-size: 0.85rem;
  color: #999;
}

.todo-actions {
  display: flex;
  gap: 0.5rem;
  flex-shrink: 0;
}

.btn-icon {
  width: 36px;
  height: 36px;
  border: none;
  border-radius: 8px;
  background: #f5f5f5;
  cursor: pointer;
  font-size: 1.1rem;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-icon:hover {
  transform: scale(1.1);
}

.btn-edit:hover {
  background: #e3f2fd;
}

.btn-delete:hover {
  background: #ffebee;
}
</style>
