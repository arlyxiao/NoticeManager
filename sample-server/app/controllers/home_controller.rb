class HomeController < ApplicationController

  def index

    img_url = 'http://192.168.1.101:3000/luffy.jpg'
    title = '测试 title'
    desc = '测试 desc'
    other = '测试 other'

    render :json => {
      :img_url => img_url, 
      :title => title,
      :desc => desc,
      :other => other
    }
  end

end
