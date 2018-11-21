const getters = {
  sidebar: state => state.app.sidebar,
  page_size: state => state.app.page_size,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  roles: state => state.user.roles,
  visitedViews: state => state.tagsView.visitedViews,

}
export default getters
